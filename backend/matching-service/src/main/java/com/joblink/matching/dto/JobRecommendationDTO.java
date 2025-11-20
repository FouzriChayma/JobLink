package com.joblink.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobRecommendationDTO {
    private Long id;
    private Long jobSeekerId;
    private Long jobId;
    private JobPostingDTO job;
    private BigDecimal matchScore;
    private String recommendationReason;
    private Boolean viewed;
    private LocalDateTime createdAt;
}

