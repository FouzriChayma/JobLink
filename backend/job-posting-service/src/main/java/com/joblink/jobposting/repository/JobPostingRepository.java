package com.joblink.jobposting.repository;

import com.joblink.jobposting.model.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    List<JobPosting> findByEmployerId(Long employerId);
    
    List<JobPosting> findByStatus(String status);
    
    @Query("SELECT j FROM JobPosting j WHERE j.status = 'ACTIVE' " +
           "AND (:location IS NULL OR j.location LIKE %:location%) " +
           "AND (:jobType IS NULL OR j.jobType = :jobType) " +
           "AND (:minSalary IS NULL OR j.salaryMin >= :minSalary) " +
           "AND (:maxSalary IS NULL OR j.salaryMax <= :maxSalary)")
    List<JobPosting> searchJobs(
        @Param("location") String location,
        @Param("jobType") String jobType,
        @Param("minSalary") BigDecimal minSalary,
        @Param("maxSalary") BigDecimal maxSalary
    );
    
    @Query("SELECT DISTINCT j FROM JobPosting j " +
           "JOIN j.requiredSkills rs " +
           "WHERE j.status = 'ACTIVE' " +
           "AND rs.skillId IN :skillIds")
    List<JobPosting> findByRequiredSkills(@Param("skillIds") List<Long> skillIds);
}

