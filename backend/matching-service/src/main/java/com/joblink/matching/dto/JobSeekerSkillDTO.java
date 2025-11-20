package com.joblink.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobSeekerSkillDTO {
    private Long skillId;
    private String proficiencyLevel;
    private Integer yearsOfExperience;
}

