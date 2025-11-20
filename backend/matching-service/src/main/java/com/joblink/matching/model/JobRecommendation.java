package com.joblink.matching.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_recommendations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobRecommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "job_seeker_id", nullable = false)
    private Long jobSeekerId;
    
    @Column(name = "job_id", nullable = false)
    private Long jobId;
    
    @Column(name = "match_score", precision = 5, scale = 2)
    private BigDecimal matchScore;
    
    @Column(name = "recommendation_reason", columnDefinition = "TEXT")
    private String recommendationReason;
    
    @Column(nullable = false)
    private Boolean viewed = false;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

