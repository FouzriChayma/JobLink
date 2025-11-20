package com.joblink.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobSeekerProfileDTO {
    private Long id;
    private String userId;
    private String firstName;
    private String lastName;
    private String phone;
    private String location;
    private String bio;
    private String cvUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<JobSeekerSkillDTO> skills;
}

