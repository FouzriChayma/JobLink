package com.joblink.matching.controller;

import com.joblink.matching.dto.JobRecommendationDTO;
import com.joblink.matching.service.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matching")
@CrossOrigin(origins = "*")
public class MatchingController {
    
    @Autowired
    private MatchingService matchingService;
    
    @PostMapping("/generate/{jobSeekerId}")
    public ResponseEntity<List<JobRecommendationDTO>> generateRecommendations(@PathVariable Long jobSeekerId) {
        List<JobRecommendationDTO> recommendations = matchingService.generateRecommendations(jobSeekerId);
        return ResponseEntity.ok(recommendations);
    }
    
    @GetMapping("/{jobSeekerId}")
    public ResponseEntity<List<JobRecommendationDTO>> getRecommendations(@PathVariable Long jobSeekerId) {
        List<JobRecommendationDTO> recommendations = matchingService.getRecommendations(jobSeekerId);
        return ResponseEntity.ok(recommendations);
    }
    
    @GetMapping("/{jobSeekerId}/unviewed")
    public ResponseEntity<List<JobRecommendationDTO>> getUnviewedRecommendations(@PathVariable Long jobSeekerId) {
        List<JobRecommendationDTO> recommendations = matchingService.getUnviewedRecommendations(jobSeekerId);
        return ResponseEntity.ok(recommendations);
    }
    
    @PutMapping("/{recommendationId}/viewed")
    public ResponseEntity<Void> markAsViewed(@PathVariable Long recommendationId) {
        matchingService.markAsViewed(recommendationId);
        return ResponseEntity.noContent().build();
    }
}

