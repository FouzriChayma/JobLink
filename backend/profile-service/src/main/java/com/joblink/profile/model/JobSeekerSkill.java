package com.joblink.profile.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_seeker_skills")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobSeekerSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "profile_id", nullable = false)
    private Long profileId;
    
    @Column(name = "skill_id", nullable = false)
    private Long skillId;
    
    @Column(name = "proficiency_level")
    private String proficiencyLevel = "BEGINNER";
    
    @Column(name = "years_of_experience")
    private Integer yearsOfExperience = 0;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private JobSeekerProfile profile;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

