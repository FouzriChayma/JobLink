package com.joblink.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationDTO {
    private Long id;
    private Long jobId;
    private Long jobSeekerId;
    private String status;
    private String coverLetter;
    private LocalDateTime appliedDate;
    private LocalDateTime updatedAt;
    private List<InterviewDTO> interviews;
}

