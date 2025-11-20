package com.joblink.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobSeekerSkillDTO {
    private Long id;
    private Long skillId;
    private String skillName;
    private String proficiencyLevel;
    private Integer yearsOfExperience;
}

