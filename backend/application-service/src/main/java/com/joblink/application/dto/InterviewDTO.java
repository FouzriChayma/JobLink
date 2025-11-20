package com.joblink.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewDTO {
    private Long id;
    private Long applicationId;
    private LocalDateTime interviewDate;
    private String interviewType;
    private String location;
    private String notes;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

