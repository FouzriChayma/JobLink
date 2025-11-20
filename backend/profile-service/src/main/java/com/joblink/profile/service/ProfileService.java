package com.joblink.profile.service;

import com.joblink.profile.dto.*;
import com.joblink.profile.model.*;
import com.joblink.profile.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileService {
    
    @Autowired
    private JobSeekerProfileRepository profileRepository;
    
    @Autowired
    private JobSeekerSkillRepository skillRepository;
    
    @Autowired
    private SkillRepository skillRepo;
    
    @Autowired
    private SkillSuggestionRepository suggestionRepository;
    
    public JobSeekerProfileDTO createOrUpdateProfile(JobSeekerProfileDTO dto) {
        JobSeekerProfile profile = profileRepository.findByUserId(dto.getUserId())
            .orElse(new JobSeekerProfile());
        
        profile.setUserId(dto.getUserId());
        profile.setFirstName(dto.getFirstName());
        profile.setLastName(dto.getLastName());
        profile.setPhone(dto.getPhone());
        profile.setLocation(dto.getLocation());
        profile.setBio(dto.getBio());
        profile.setCvUrl(dto.getCvUrl());
        
        JobSeekerProfile saved = profileRepository.save(profile);
        return convertToDTO(saved);
    }
    
    public JobSeekerProfileDTO getProfileByUserId(String userId) {
        JobSeekerProfile profile = profileRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("Profile not found"));
        return convertToDTO(profile);
    }
    
    public JobSeekerProfileDTO getProfileById(Long id) {
        JobSeekerProfile profile = profileRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Profile not found"));
        return convertToDTO(profile);
    }
    
    @Transactional
    public JobSeekerSkillDTO addSkill(Long profileId, JobSeekerSkillDTO skillDTO) {
        JobSeekerProfile profile = profileRepository.findById(profileId)
            .orElseThrow(() -> new RuntimeException("Profile not found"));
        
        JobSeekerSkill skill = new JobSeekerSkill();
        skill.setProfileId(profileId);
        skill.setSkillId(skillDTO.getSkillId());
        skill.setProficiencyLevel(skillDTO.getProficiencyLevel());
        skill.setYearsOfExperience(skillDTO.getYearsOfExperience());
        
        JobSeekerSkill saved = skillRepository.save(skill);
        return convertSkillToDTO(saved);
    }
    
    public void removeSkill(Long skillId) {
        skillRepository.deleteById(skillId);
    }
    
    public List<SkillDTO> getAllSkills() {
        return skillRepo.findAll().stream()
            .map(skill -> new SkillDTO(skill.getId(), skill.getName(), skill.getCategory()))
            .collect(Collectors.toList());
    }
    
    public List<SkillSuggestionDTO> getSkillSuggestions(Long jobSeekerId) {
        return suggestionRepository.findByJobSeekerId(jobSeekerId).stream()
            .map(this::convertSuggestionToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public SkillSuggestionDTO createSkillSuggestion(SkillSuggestionDTO dto) {
        SkillSuggestion suggestion = new SkillSuggestion();
        suggestion.setJobSeekerId(dto.getJobSeekerId());
        suggestion.setSkillId(dto.getSkillId());
        suggestion.setSuggestedCourse(dto.getSuggestedCourse());
        suggestion.setSuggestedCertification(dto.getSuggestedCertification());
        suggestion.setReason(dto.getReason());
        suggestion.setPriority(dto.getPriority());
        
        SkillSuggestion saved = suggestionRepository.save(suggestion);
        return convertSuggestionToDTO(saved);
    }
    
    private JobSeekerProfileDTO convertToDTO(JobSeekerProfile profile) {
        JobSeekerProfileDTO dto = new JobSeekerProfileDTO();
        dto.setId(profile.getId());
        dto.setUserId(profile.getUserId());
        dto.setFirstName(profile.getFirstName());
        dto.setLastName(profile.getLastName());
        dto.setPhone(profile.getPhone());
        dto.setLocation(profile.getLocation());
        dto.setBio(profile.getBio());
        dto.setCvUrl(profile.getCvUrl());
        dto.setCreatedAt(profile.getCreatedAt());
        dto.setUpdatedAt(profile.getUpdatedAt());
        
        if (profile.getSkills() != null) {
            List<JobSeekerSkillDTO> skillDTOs = profile.getSkills().stream()
                .map(this::convertSkillToDTO)
                .collect(Collectors.toList());
            dto.setSkills(skillDTOs);
        }
        
        return dto;
    }
    
    private JobSeekerSkillDTO convertSkillToDTO(JobSeekerSkill skill) {
        JobSeekerSkillDTO dto = new JobSeekerSkillDTO();
        dto.setId(skill.getId());
        dto.setSkillId(skill.getSkillId());
        dto.setProficiencyLevel(skill.getProficiencyLevel());
        dto.setYearsOfExperience(skill.getYearsOfExperience());
        
        skillRepo.findById(skill.getSkillId()).ifPresent(s -> dto.setSkillName(s.getName()));
        
        return dto;
    }
    
    private SkillSuggestionDTO convertSuggestionToDTO(SkillSuggestion suggestion) {
        SkillSuggestionDTO dto = new SkillSuggestionDTO();
        dto.setId(suggestion.getId());
        dto.setJobSeekerId(suggestion.getJobSeekerId());
        dto.setSkillId(suggestion.getSkillId());
        dto.setSuggestedCourse(suggestion.getSuggestedCourse());
        dto.setSuggestedCertification(suggestion.getSuggestedCertification());
        dto.setReason(suggestion.getReason());
        dto.setPriority(suggestion.getPriority());
        dto.setCreatedAt(suggestion.getCreatedAt());
        
        skillRepo.findById(suggestion.getSkillId()).ifPresent(s -> dto.setSkillName(s.getName()));
        
        return dto;
    }
}

