package com.joblink.jobposting.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_required_skills")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobRequiredSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "job_id", nullable = false)
    private Long jobId;
    
    @Column(name = "skill_id", nullable = false)
    private Long skillId;
    
    @Column(name = "required_proficiency")
    private String requiredProficiency = "BEGINNER";
    
    @Column(name = "is_mandatory")
    private Boolean isMandatory = true;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", insertable = false, updatable = false)
    private JobPosting job;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

