package com.joblink.matching.service;

import com.joblink.matching.dto.*;
import com.joblink.matching.model.JobRecommendation;
import com.joblink.matching.repository.JobRecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchingService {
    
    @Autowired
    private JobRecommendationRepository recommendationRepository;
    
    @Value("${job-posting-service.url}")
    private String jobPostingServiceUrl;
    
    @Value("${profile-service.url}")
    private String profileServiceUrl;
    
    private final WebClient webClient;
    
    public MatchingService() {
        this.webClient = WebClient.builder().build();
    }
    
    public List<JobRecommendationDTO> generateRecommendations(Long jobSeekerId) {
        // Fetch job seeker profile
        JobSeekerProfileDTO profile = fetchJobSeekerProfile(jobSeekerId);
        if (profile == null || profile.getSkills() == null || profile.getSkills().isEmpty()) {
            return Collections.emptyList();
        }
        
        // Fetch all active jobs
        List<JobPostingDTO> jobs = fetchAllActiveJobs();
        
        // Calculate match scores
        List<JobRecommendationDTO> recommendations = new ArrayList<>();
        for (JobPostingDTO job : jobs) {
            BigDecimal matchScore = calculateMatchScore(profile, job);
            if (matchScore.compareTo(BigDecimal.valueOf(30)) > 0) { // Only recommend if match > 30%
                JobRecommendationDTO recommendation = new JobRecommendationDTO();
                recommendation.setJobSeekerId(jobSeekerId);
                recommendation.setJobId(job.getId());
                recommendation.setJob(job);
                recommendation.setMatchScore(matchScore);
                recommendation.setRecommendationReason(generateReason(profile, job, matchScore));
                recommendation.setViewed(false);
                
                // Save to database
                JobRecommendation entity = new JobRecommendation();
                entity.setJobSeekerId(jobSeekerId);
                entity.setJobId(job.getId());
                entity.setMatchScore(matchScore);
                entity.setRecommendationReason(recommendation.getRecommendationReason());
                entity.setViewed(false);
                recommendationRepository.save(entity);
                
                recommendations.add(recommendation);
            }
        }
        
        // Sort by match score descending
        recommendations.sort((a, b) -> b.getMatchScore().compareTo(a.getMatchScore()));
        
        return recommendations;
    }
    
    public List<JobRecommendationDTO> getRecommendations(Long jobSeekerId) {
        List<JobRecommendation> recommendations = recommendationRepository.findByJobSeekerId(jobSeekerId);
        return recommendations.stream()
            .map(this::convertToDTO)
            .sorted((a, b) -> b.getMatchScore().compareTo(a.getMatchScore()))
            .collect(Collectors.toList());
    }
    
    public List<JobRecommendationDTO> getUnviewedRecommendations(Long jobSeekerId) {
        List<JobRecommendation> recommendations = recommendationRepository.findByJobSeekerIdAndViewed(jobSeekerId, false);
        return recommendations.stream()
            .map(this::convertToDTO)
            .sorted((a, b) -> b.getMatchScore().compareTo(a.getMatchScore()))
            .collect(Collectors.toList());
    }
    
    public void markAsViewed(Long recommendationId) {
        JobRecommendation recommendation = recommendationRepository.findById(recommendationId)
            .orElseThrow(() -> new RuntimeException("Recommendation not found"));
        recommendation.setViewed(true);
        recommendationRepository.save(recommendation);
    }
    
    private BigDecimal calculateMatchScore(JobSeekerProfileDTO profile, JobPostingDTO job) {
        if (job.getRequiredSkills() == null || job.getRequiredSkills().isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        Set<Long> jobSeekerSkillIds = profile.getSkills().stream()
            .map(JobSeekerSkillDTO::getSkillId)
            .collect(Collectors.toSet());
        
        int totalSkills = job.getRequiredSkills().size();
        int matchedSkills = 0;
        int mandatoryMatched = 0;
        int totalMandatory = 0;
        
        for (JobRequiredSkillDTO requiredSkill : job.getRequiredSkills()) {
            if (requiredSkill.getIsMandatory()) {
                totalMandatory++;
            }
            
            if (jobSeekerSkillIds.contains(requiredSkill.getSkillId())) {
                matchedSkills++;
                if (requiredSkill.getIsMandatory()) {
                    mandatoryMatched++;
                }
            }
        }
        
        // Base score: percentage of matched skills
        BigDecimal baseScore = BigDecimal.valueOf(matchedSkills)
            .divide(BigDecimal.valueOf(totalSkills), 2, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100));
        
        // Bonus for matching mandatory skills
        BigDecimal mandatoryBonus = BigDecimal.ZERO;
        if (totalMandatory > 0) {
            mandatoryBonus = BigDecimal.valueOf(mandatoryMatched)
                .divide(BigDecimal.valueOf(totalMandatory), 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(20));
        }
        
        // Location match bonus
        BigDecimal locationBonus = BigDecimal.ZERO;
        if (profile.getLocation() != null && job.getLocation() != null &&
            profile.getLocation().equalsIgnoreCase(job.getLocation())) {
            locationBonus = BigDecimal.valueOf(10);
        }
        
        BigDecimal totalScore = baseScore.add(mandatoryBonus).add(locationBonus);
        return totalScore.min(BigDecimal.valueOf(100)); // Cap at 100
    }
    
    private String generateReason(JobSeekerProfileDTO profile, JobPostingDTO job, BigDecimal matchScore) {
        StringBuilder reason = new StringBuilder();
        reason.append("Match score: ").append(matchScore).append("%. ");
        
        Set<Long> jobSeekerSkillIds = profile.getSkills().stream()
            .map(JobSeekerSkillDTO::getSkillId)
            .collect(Collectors.toSet());
        
        long matchedCount = job.getRequiredSkills().stream()
            .filter(skill -> jobSeekerSkillIds.contains(skill.getSkillId()))
            .count();
        
        reason.append("You have ").append(matchedCount).append(" out of ")
            .append(job.getRequiredSkills().size()).append(" required skills.");
        
        if (profile.getLocation() != null && job.getLocation() != null &&
            profile.getLocation().equalsIgnoreCase(job.getLocation())) {
            reason.append(" Location matches your preference.");
        }
        
        return reason.toString();
    }
    
    private JobSeekerProfileDTO fetchJobSeekerProfile(Long jobSeekerId) {
        try {
            return webClient.get()
                .uri(profileServiceUrl + "/api/profiles/{id}", jobSeekerId)
                .retrieve()
                .bodyToMono(JobSeekerProfileDTO.class)
                .block();
        } catch (Exception e) {
            return null;
        }
    }
    
    private List<JobPostingDTO> fetchAllActiveJobs() {
        try {
            JobPostingDTO[] jobs = webClient.get()
                .uri(jobPostingServiceUrl + "/api/jobs")
                .retrieve()
                .bodyToMono(JobPostingDTO[].class)
                .block();
            return jobs != null ? Arrays.asList(jobs) : Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
    
    private JobRecommendationDTO convertToDTO(JobRecommendation entity) {
        JobRecommendationDTO dto = new JobRecommendationDTO();
        dto.setId(entity.getId());
        dto.setJobSeekerId(entity.getJobSeekerId());
        dto.setJobId(entity.getJobId());
        dto.setMatchScore(entity.getMatchScore());
        dto.setRecommendationReason(entity.getRecommendationReason());
        dto.setViewed(entity.getViewed());
        dto.setCreatedAt(entity.getCreatedAt());
        
        // Fetch job details
        try {
            JobPostingDTO job = webClient.get()
                .uri(jobPostingServiceUrl + "/api/jobs/{id}", entity.getJobId())
                .retrieve()
                .bodyToMono(JobPostingDTO.class)
                .block();
            dto.setJob(job);
        } catch (Exception e) {
            // Job not found or service unavailable
        }
        
        return dto;
    }
}

