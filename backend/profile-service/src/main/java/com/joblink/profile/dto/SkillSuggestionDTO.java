package com.joblink.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillSuggestionDTO {
    private Long id;
    private Long jobSeekerId;
    private Long skillId;
    private String skillName;
    private String suggestedCourse;
    private String suggestedCertification;
    private String reason;
    private String priority;
    private LocalDateTime createdAt;
}

