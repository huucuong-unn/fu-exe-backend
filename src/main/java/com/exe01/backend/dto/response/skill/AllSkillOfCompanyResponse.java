package com.exe01.backend.dto.response.skill;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class AllSkillOfCompanyResponse implements Serializable {

    private String skillName;

    public AllSkillOfCompanyResponse(String skillName) {
        this.skillName = skillName;
    }

}
