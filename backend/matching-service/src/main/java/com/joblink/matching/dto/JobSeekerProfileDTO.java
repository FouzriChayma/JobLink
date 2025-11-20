package com.joblink.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobSeekerProfileDTO {
    private Long id;
    private String userId;
    private String location;
    private List<JobSeekerSkillDTO> skills;
}

