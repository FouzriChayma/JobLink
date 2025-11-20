package com.joblink.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobPostingDTO {
    private Long id;
    private Long employerId;
    private String title;
    private String description;
    private String location;
    private String jobType;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String status;
    private LocalDateTime postedDate;
    private List<JobRequiredSkillDTO> requiredSkills;
}

