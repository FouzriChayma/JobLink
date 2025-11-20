package com.joblink.application.controller;

import com.joblink.application.dto.JobApplicationDTO;
import com.joblink.application.dto.InterviewDTO;
import com.joblink.application.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*")
public class ApplicationController {
    
    @Autowired
    private ApplicationService applicationService;
    
    @PostMapping
    @PreAuthorize("hasRole('job_seeker') or hasRole('admin')")
    public ResponseEntity<JobApplicationDTO> createApplication(@RequestBody JobApplicationDTO applicationDTO) {
        JobApplicationDTO created = applicationService.createApplication(applicationDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<JobApplicationDTO> getApplicationById(@PathVariable Long id) {
        JobApplicationDTO application = applicationService.getApplicationById(id);
        return ResponseEntity.ok(application);
    }
    
    @GetMapping("/job-seeker/{jobSeekerId}")
    @PreAuthorize("hasRole('job_seeker') or hasRole('admin')")
    public ResponseEntity<List<JobApplicationDTO>> getApplicationsByJobSeeker(@PathVariable Long jobSeekerId) {
        List<JobApplicationDTO> applications = applicationService.getApplicationsByJobSeeker(jobSeekerId);
        return ResponseEntity.ok(applications);
    }
    
    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasRole('employer') or hasRole('admin')")
    public ResponseEntity<List<JobApplicationDTO>> getApplicationsByJob(@PathVariable Long jobId) {
        List<JobApplicationDTO> applications = applicationService.getApplicationsByJob(jobId);
        return ResponseEntity.ok(applications);
    }
    
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('employer') or hasRole('admin')")
    public ResponseEntity<JobApplicationDTO> updateApplicationStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        JobApplicationDTO updated = applicationService.updateApplicationStatus(id, status);
        return ResponseEntity.ok(updated);
    }
    
    @PostMapping("/interviews")
    @PreAuthorize("hasRole('employer') or hasRole('admin')")
    public ResponseEntity<InterviewDTO> scheduleInterview(@RequestBody InterviewDTO interviewDTO) {
        InterviewDTO created = applicationService.scheduleInterview(interviewDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    
    @GetMapping("/{applicationId}/interviews")
    public ResponseEntity<List<InterviewDTO>> getInterviewsByApplication(@PathVariable Long applicationId) {
        List<InterviewDTO> interviews = applicationService.getInterviewsByApplication(applicationId);
        return ResponseEntity.ok(interviews);
    }
    
    @PutMapping("/interviews/{id}")
    @PreAuthorize("hasRole('employer') or hasRole('admin')")
    public ResponseEntity<InterviewDTO> updateInterview(
            @PathVariable Long id,
            @RequestBody InterviewDTO interviewDTO) {
        InterviewDTO updated = applicationService.updateInterview(id, interviewDTO);
        return ResponseEntity.ok(updated);
    }
}

