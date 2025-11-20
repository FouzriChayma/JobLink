package com.joblink.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobRequiredSkillDTO {
    private Long skillId;
    private String requiredProficiency;
    private Boolean isMandatory;
}

