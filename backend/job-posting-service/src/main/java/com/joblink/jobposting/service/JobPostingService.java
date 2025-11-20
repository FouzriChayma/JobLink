package com.joblink.jobposting.service;

import com.joblink.jobposting.dto.JobPostingDTO;
import com.joblink.jobposting.dto.JobRequiredSkillDTO;
import com.joblink.jobposting.model.JobPosting;
import com.joblink.jobposting.model.JobRequiredSkill;
import com.joblink.jobposting.repository.JobPostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobPostingService {
    
    @Autowired
    private JobPostingRepository jobPostingRepository;
    
    public JobPostingDTO createJobPosting(JobPostingDTO dto) {
        JobPosting jobPosting = convertToEntity(dto);
        JobPosting saved = jobPostingRepository.save(jobPosting);
        return convertToDTO(saved);
    }
    
    public JobPostingDTO getJobPostingById(Long id) {
        JobPosting jobPosting = jobPostingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Job posting not found"));
        return convertToDTO(jobPosting);
    }
    
    public List<JobPostingDTO> getAllActiveJobs() {
        return jobPostingRepository.findByStatus("ACTIVE")
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<JobPostingDTO> getJobsByEmployer(Long employerId) {
        return jobPostingRepository.findByEmployerId(employerId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<JobPostingDTO> searchJobs(String location, String jobType, 
                                         BigDecimal minSalary, BigDecimal maxSalary) {
        return jobPostingRepository.searchJobs(location, jobType, minSalary, maxSalary)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<JobPostingDTO> searchJobsBySkills(List<Long> skillIds) {
        return jobPostingRepository.findByRequiredSkills(skillIds)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public JobPostingDTO updateJobPosting(Long id, JobPostingDTO dto) {
        JobPosting existing = jobPostingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Job posting not found"));
        
        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setLocation(dto.getLocation());
        existing.setJobType(dto.getJobType());
        existing.setSalaryMin(dto.getSalaryMin());
        existing.setSalaryMax(dto.getSalaryMax());
        existing.setStatus(dto.getStatus());
        
        JobPosting updated = jobPostingRepository.save(existing);
        return convertToDTO(updated);
    }
    
    public void deleteJobPosting(Long id) {
        jobPostingRepository.deleteById(id);
    }
    
    private JobPosting convertToEntity(JobPostingDTO dto) {
        JobPosting jobPosting = new JobPosting();
        jobPosting.setId(dto.getId());
        jobPosting.setEmployerId(dto.getEmployerId());
        jobPosting.setTitle(dto.getTitle());
        jobPosting.setDescription(dto.getDescription());
        jobPosting.setLocation(dto.getLocation());
        jobPosting.setJobType(dto.getJobType());
        jobPosting.setSalaryMin(dto.getSalaryMin());
        jobPosting.setSalaryMax(dto.getSalaryMax());
        jobPosting.setStatus(dto.getStatus() != null ? dto.getStatus() : "ACTIVE");
        jobPosting.setExpiryDate(dto.getExpiryDate());
        
        if (dto.getRequiredSkills() != null) {
            List<JobRequiredSkill> skills = dto.getRequiredSkills().stream()
                .map(skillDto -> {
                    JobRequiredSkill skill = new JobRequiredSkill();
                    skill.setSkillId(skillDto.getSkillId());
                    skill.setRequiredProficiency(skillDto.getRequiredProficiency());
                    skill.setIsMandatory(skillDto.getIsMandatory());
                    skill.setJobId(jobPosting.getId());
                    return skill;
                })
                .collect(Collectors.toList());
            jobPosting.setRequiredSkills(skills);
        }
        
        return jobPosting;
    }
    
    private JobPostingDTO convertToDTO(JobPosting jobPosting) {
        JobPostingDTO dto = new JobPostingDTO();
        dto.setId(jobPosting.getId());
        dto.setEmployerId(jobPosting.getEmployerId());
        dto.setTitle(jobPosting.getTitle());
        dto.setDescription(jobPosting.getDescription());
        dto.setLocation(jobPosting.getLocation());
        dto.setJobType(jobPosting.getJobType());
        dto.setSalaryMin(jobPosting.getSalaryMin());
        dto.setSalaryMax(jobPosting.getSalaryMax());
        dto.setStatus(jobPosting.getStatus());
        dto.setPostedDate(jobPosting.getPostedDate());
        dto.setExpiryDate(jobPosting.getExpiryDate());
        
        if (jobPosting.getRequiredSkills() != null) {
            List<JobRequiredSkillDTO> skillDTOs = jobPosting.getRequiredSkills().stream()
                .map(skill -> {
                    JobRequiredSkillDTO skillDto = new JobRequiredSkillDTO();
                    skillDto.setSkillId(skill.getSkillId());
                    skillDto.setRequiredProficiency(skill.getRequiredProficiency());
                    skillDto.setIsMandatory(skill.getIsMandatory());
                    return skillDto;
                })
                .collect(Collectors.toList());
            dto.setRequiredSkills(skillDTOs);
        }
        
        return dto;
    }
}

