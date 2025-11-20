package com.joblink.jobposting.controller;

import com.joblink.jobposting.dto.JobPostingDTO;
import com.joblink.jobposting.service.JobPostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "*")
public class JobPostingController {
    
    @Autowired
    private JobPostingService jobPostingService;
    
    @PostMapping
    @PreAuthorize("hasRole('employer') or hasRole('admin')")
    public ResponseEntity<JobPostingDTO> createJobPosting(@RequestBody JobPostingDTO jobPostingDTO) {
        JobPostingDTO created = jobPostingService.createJobPosting(jobPostingDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<JobPostingDTO>> getAllJobs() {
        List<JobPostingDTO> jobs = jobPostingService.getAllActiveJobs();
        return ResponseEntity.ok(jobs);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<JobPostingDTO> getJobById(@PathVariable Long id) {
        JobPostingDTO job = jobPostingService.getJobPostingById(id);
        return ResponseEntity.ok(job);
    }
    
    @GetMapping("/employer/{employerId}")
    @PreAuthorize("hasRole('employer') or hasRole('admin')")
    public ResponseEntity<List<JobPostingDTO>> getJobsByEmployer(@PathVariable Long employerId) {
        List<JobPostingDTO> jobs = jobPostingService.getJobsByEmployer(employerId);
        return ResponseEntity.ok(jobs);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<JobPostingDTO>> searchJobs(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String jobType,
            @RequestParam(required = false) BigDecimal minSalary,
            @RequestParam(required = false) BigDecimal maxSalary) {
        List<JobPostingDTO> jobs = jobPostingService.searchJobs(location, jobType, minSalary, maxSalary);
        return ResponseEntity.ok(jobs);
    }
    
    @PostMapping("/search/skills")
    public ResponseEntity<List<JobPostingDTO>> searchJobsBySkills(@RequestBody List<Long> skillIds) {
        List<JobPostingDTO> jobs = jobPostingService.searchJobsBySkills(skillIds);
        return ResponseEntity.ok(jobs);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('employer') or hasRole('admin')")
    public ResponseEntity<JobPostingDTO> updateJobPosting(
            @PathVariable Long id,
            @RequestBody JobPostingDTO jobPostingDTO) {
        JobPostingDTO updated = jobPostingService.updateJobPosting(id, jobPostingDTO);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('employer') or hasRole('admin')")
    public ResponseEntity<Void> deleteJobPosting(@PathVariable Long id) {
        jobPostingService.deleteJobPosting(id);
        return ResponseEntity.noContent().build();
    }
}

