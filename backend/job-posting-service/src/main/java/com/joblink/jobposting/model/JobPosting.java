package com.joblink.jobposting.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "job_postings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobPosting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "employer_id", nullable = false)
    private Long employerId;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private String location;
    
    @Column(name = "job_type")
    private String jobType;
    
    @Column(name = "salary_min", precision = 10, scale = 2)
    private BigDecimal salaryMin;
    
    @Column(name = "salary_max", precision = 10, scale = 2)
    private BigDecimal salaryMax;
    
    @Column(nullable = false)
    private String status = "ACTIVE";
    
    @Column(name = "posted_date")
    private LocalDateTime postedDate;
    
    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<JobRequiredSkill> requiredSkills;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        postedDate = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

