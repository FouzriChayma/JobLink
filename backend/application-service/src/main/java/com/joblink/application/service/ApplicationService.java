package com.joblink.application.service;

import com.joblink.application.dto.JobApplicationDTO;
import com.joblink.application.dto.InterviewDTO;
import com.joblink.application.model.JobApplication;
import com.joblink.application.model.Interview;
import com.joblink.application.repository.JobApplicationRepository;
import com.joblink.application.repository.InterviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {
    
    @Autowired
    private JobApplicationRepository applicationRepository;
    
    @Autowired
    private InterviewRepository interviewRepository;
    
    @Transactional
    public JobApplicationDTO createApplication(JobApplicationDTO dto) {
        // Check if application already exists
        applicationRepository.findByJobIdAndJobSeekerId(dto.getJobId(), dto.getJobSeekerId())
            .ifPresent(app -> {
                throw new RuntimeException("Application already exists");
            });
        
        JobApplication application = new JobApplication();
        application.setJobId(dto.getJobId());
        application.setJobSeekerId(dto.getJobSeekerId());
        application.setStatus(dto.getStatus() != null ? dto.getStatus() : "PENDING");
        application.setCoverLetter(dto.getCoverLetter());
        
        JobApplication saved = applicationRepository.save(application);
        return convertToDTO(saved);
    }
    
    public JobApplicationDTO getApplicationById(Long id) {
        JobApplication application = applicationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Application not found"));
        return convertToDTO(application);
    }
    
    public List<JobApplicationDTO> getApplicationsByJobSeeker(Long jobSeekerId) {
        return applicationRepository.findByJobSeekerId(jobSeekerId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<JobApplicationDTO> getApplicationsByJob(Long jobId) {
        return applicationRepository.findByJobId(jobId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public JobApplicationDTO updateApplicationStatus(Long id, String status) {
        JobApplication application = applicationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Application not found"));
        application.setStatus(status);
        JobApplication updated = applicationRepository.save(application);
        return convertToDTO(updated);
    }
    
    @Transactional
    public InterviewDTO scheduleInterview(InterviewDTO dto) {
        Interview interview = new Interview();
        interview.setApplicationId(dto.getApplicationId());
        interview.setInterviewDate(dto.getInterviewDate());
        interview.setInterviewType(dto.getInterviewType());
        interview.setLocation(dto.getLocation());
        interview.setNotes(dto.getNotes());
        interview.setStatus(dto.getStatus() != null ? dto.getStatus() : "SCHEDULED");
        
        Interview saved = interviewRepository.save(interview);
        return convertInterviewToDTO(saved);
    }
    
    public List<InterviewDTO> getInterviewsByApplication(Long applicationId) {
        return interviewRepository.findByApplicationId(applicationId).stream()
            .map(this::convertInterviewToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public InterviewDTO updateInterview(Long id, InterviewDTO dto) {
        Interview interview = interviewRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Interview not found"));
        
        interview.setInterviewDate(dto.getInterviewDate());
        interview.setInterviewType(dto.getInterviewType());
        interview.setLocation(dto.getLocation());
        interview.setNotes(dto.getNotes());
        interview.setStatus(dto.getStatus());
        
        Interview updated = interviewRepository.save(interview);
        return convertInterviewToDTO(updated);
    }
    
    private JobApplicationDTO convertToDTO(JobApplication application) {
        JobApplicationDTO dto = new JobApplicationDTO();
        dto.setId(application.getId());
        dto.setJobId(application.getJobId());
        dto.setJobSeekerId(application.getJobSeekerId());
        dto.setStatus(application.getStatus());
        dto.setCoverLetter(application.getCoverLetter());
        dto.setAppliedDate(application.getAppliedDate());
        dto.setUpdatedAt(application.getUpdatedAt());
        
        List<InterviewDTO> interviews = interviewRepository.findByApplicationId(application.getId()).stream()
            .map(this::convertInterviewToDTO)
            .collect(Collectors.toList());
        dto.setInterviews(interviews);
        
        return dto;
    }
    
    private InterviewDTO convertInterviewToDTO(Interview interview) {
        InterviewDTO dto = new InterviewDTO();
        dto.setId(interview.getId());
        dto.setApplicationId(interview.getApplicationId());
        dto.setInterviewDate(interview.getInterviewDate());
        dto.setInterviewType(interview.getInterviewType());
        dto.setLocation(interview.getLocation());
        dto.setNotes(interview.getNotes());
        dto.setStatus(interview.getStatus());
        dto.setCreatedAt(interview.getCreatedAt());
        dto.setUpdatedAt(interview.getUpdatedAt());
        return dto;
    }
}

