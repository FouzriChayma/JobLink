package com.joblink.application.repository;

import com.joblink.application.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByJobSeekerId(Long jobSeekerId);
    List<JobApplication> findByJobId(Long jobId);
    List<JobApplication> findByStatus(String status);
    Optional<JobApplication> findByJobIdAndJobSeekerId(Long jobId, Long jobSeekerId);
}

