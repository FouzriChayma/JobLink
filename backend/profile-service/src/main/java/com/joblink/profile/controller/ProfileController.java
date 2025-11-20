package com.joblink.profile.controller;

import com.joblink.profile.dto.*;
import com.joblink.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
@CrossOrigin(origins = "*")
public class ProfileController {
    
    @Autowired
    private ProfileService profileService;
    
    @PostMapping
    @PreAuthorize("hasRole('job_seeker') or hasRole('admin')")
    public ResponseEntity<JobSeekerProfileDTO> createOrUpdateProfile(@RequestBody JobSeekerProfileDTO profileDTO) {
        JobSeekerProfileDTO saved = profileService.createOrUpdateProfile(profileDTO);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<JobSeekerProfileDTO> getProfileByUserId(@PathVariable String userId) {
        JobSeekerProfileDTO profile = profileService.getProfileByUserId(userId);
        return ResponseEntity.ok(profile);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<JobSeekerProfileDTO> getProfileById(@PathVariable Long id) {
        JobSeekerProfileDTO profile = profileService.getProfileById(id);
        return ResponseEntity.ok(profile);
    }
    
    @PostMapping("/{profileId}/skills")
    @PreAuthorize("hasRole('job_seeker') or hasRole('admin')")
    public ResponseEntity<JobSeekerSkillDTO> addSkill(
            @PathVariable Long profileId,
            @RequestBody JobSeekerSkillDTO skillDTO) {
        JobSeekerSkillDTO saved = profileService.addSkill(profileId, skillDTO);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
    
    @DeleteMapping("/skills/{skillId}")
    @PreAuthorize("hasRole('job_seeker') or hasRole('admin')")
    public ResponseEntity<Void> removeSkill(@PathVariable Long skillId) {
        profileService.removeSkill(skillId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/skills")
    public ResponseEntity<List<SkillDTO>> getAllSkills() {
        List<SkillDTO> skills = profileService.getAllSkills();
        return ResponseEntity.ok(skills);
    }
    
    @GetMapping("/{jobSeekerId}/suggestions")
    @PreAuthorize("hasRole('job_seeker') or hasRole('admin')")
    public ResponseEntity<List<SkillSuggestionDTO>> getSkillSuggestions(@PathVariable Long jobSeekerId) {
        List<SkillSuggestionDTO> suggestions = profileService.getSkillSuggestions(jobSeekerId);
        return ResponseEntity.ok(suggestions);
    }
    
    @PostMapping("/suggestions")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<SkillSuggestionDTO> createSkillSuggestion(@RequestBody SkillSuggestionDTO dto) {
        SkillSuggestionDTO saved = profileService.createSkillSuggestion(dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
}

