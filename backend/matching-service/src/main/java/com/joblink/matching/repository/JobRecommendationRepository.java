package com.joblink.matching.repository;

import com.joblink.matching.model.JobRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRecommendationRepository extends JpaRepository<JobRecommendation, Long> {
    List<JobRecommendation> findByJobSeekerId(Long jobSeekerId);
    List<JobRecommendation> findByJobSeekerIdAndViewed(Long jobSeekerId, Boolean viewed);
}

