package com.joblink.gateway.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Value("${keycloak.server-url:http://keycloak:8080}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm:joblink}")
    private String realm;

    @Value("${keycloak.admin.username:admin}")
    private String adminUsername;

    @Value("${keycloak.admin.password:admin}")
    private String adminPassword;

    private final WebClient webClient = WebClient.builder().build();

    @PostMapping("/signup")
    public Mono<ResponseEntity<?>> signup(@RequestBody SignupRequest request) {
        return getAdminToken()
            .flatMap(adminToken -> {
                if (adminToken == null) {
                    ResponseEntity<?> errorResponse = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Failed to authenticate with Keycloak admin"));
                    return Mono.just(errorResponse);
                }
                
                return createUser(adminToken, request)
                    .flatMap(userId -> {
                        if (userId == null) {
                            ResponseEntity<?> errorResponse = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(Map.of("error", "Failed to create user. Username or email may already exist."));
                            return Mono.just(errorResponse);
                        }
                        
                        return assignRole(adminToken, userId, request.getUserType())
                            .then(Mono.just((ResponseEntity<?>) ResponseEntity.ok(Map.of(
                                "message", "User created successfully",
                                "userId", userId,
                                "username", request.getUsername()
                            ))));
                    });
            })
            .onErrorResume(e -> {
                ResponseEntity<?> errorResponse = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create user: " + e.getMessage()));
                return Mono.just(errorResponse);
            });
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<?>> login(@RequestBody LoginRequest request) {
        String tokenUrl = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";
        
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", "joblink-frontend");
        params.add("username", request.getUsername());
        params.add("password", request.getPassword());

        return webClient.post()
            .uri(tokenUrl)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(params))
            .retrieve()
            .bodyToMono(Map.class)
            .map(response -> {
                Map<String, Object> result = new HashMap<>();
                result.put("access_token", response.get("access_token"));
                result.put("refresh_token", response.get("refresh_token"));
                result.put("token_type", response.get("token_type"));
                result.put("expires_in", response.get("expires_in"));
                return ResponseEntity.ok(result);
            })
            .<ResponseEntity<?>>map(response -> response)
            .onErrorResume(e -> {
                Map<String, String> errorBody = Map.of("error", "Invalid username or password");
                ResponseEntity<?> errorResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(errorBody);
                return Mono.just(errorResponse);
            });
    }

    private Mono<String> getAdminToken() {
        String tokenUrl = keycloakServerUrl + "/realms/master/protocol/openid-connect/token";
        
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", "admin-cli");
        params.add("username", adminUsername);
        params.add("password", adminPassword);

        return webClient.post()
            .uri(tokenUrl)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(params))
            .retrieve()
            .bodyToMono(Map.class)
            .map(response -> (String) response.get("access_token"))
            .onErrorReturn(null);
    }

    private Mono<String> createUser(String adminToken, SignupRequest request) {
        String userUrl = keycloakServerUrl + "/admin/realms/" + realm + "/users";

        Map<String, Object> user = new HashMap<>();
        user.put("username", request.getUsername());
        user.put("email", request.getEmail());
        user.put("firstName", request.getFirstName());
        user.put("lastName", request.getLastName());
        user.put("enabled", true);
        user.put("emailVerified", false);

        Map<String, Object> credentials = new HashMap<>();
        credentials.put("type", "password");
        credentials.put("value", request.getPassword());
        credentials.put("temporary", false);
        user.put("credentials", Collections.singletonList(credentials));

        return webClient.post()
            .uri(userUrl)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(user)
            .exchangeToMono(response -> {
                if (response.statusCode() == HttpStatus.CREATED) {
                    String location = response.headers().asHttpHeaders().getFirst("Location");
                    if (location != null) {
                        return Mono.just(location.substring(location.lastIndexOf("/") + 1));
                    }
                }
                return Mono.just((String) null);
            })
            .onErrorReturn(null);
    }

    private Mono<Void> assignRole(String adminToken, String userId, String roleName) {
        // Get role
        String roleUrl = keycloakServerUrl + "/admin/realms/" + realm + "/roles/" + roleName;
        
        return webClient.get()
            .uri(roleUrl)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
            .retrieve()
            .bodyToMono(Map.class)
            .flatMap(role -> {
                // Assign role to user
                String assignRoleUrl = keycloakServerUrl + "/admin/realms/" + realm + 
                    "/users/" + userId + "/role-mappings/realm";
                
                return webClient.post()
                    .uri(assignRoleUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Collections.singletonList(role))
                    .retrieve()
                    .bodyToMono(Void.class);
            })
            .onErrorComplete();
    }

    // Inner class for request body
    public static class SignupRequest {
        private String username;
        private String email;
        private String password;
        private String firstName;
        private String lastName;
        private String userType;

        // Getters and setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        
        public String getUserType() { return userType; }
        public void setUserType(String userType) { this.userType = userType; }
    }

    // Inner class for login request
    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}

