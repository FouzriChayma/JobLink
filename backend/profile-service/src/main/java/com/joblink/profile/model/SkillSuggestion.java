package com.joblink.profile.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "skill_suggestions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillSuggestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "job_seeker_id", nullable = false)
    private Long jobSeekerId;
    
    @Column(name = "skill_id", nullable = false)
    private Long skillId;
    
    @Column(name = "suggested_course")
    private String suggestedCourse;
    
    @Column(name = "suggested_certification")
    private String suggestedCertification;
    
    @Column(columnDefinition = "TEXT")
    private String reason;
    
    private String priority = "MEDIUM";
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

