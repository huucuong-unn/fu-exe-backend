package com.exe01.backend.converter;

import com.exe01.backend.dto.SkillDTO;
import com.exe01.backend.entity.Skill;

public class SkillConverter {

    public static SkillDTO toDTO(Skill skill) {
        SkillDTO skillDTO = new SkillDTO();
        skillDTO.setId(skill.getId());
        skillDTO.setName(skill.getName());
        skillDTO.setMajor(MajorConverter.toDto(skill.getMajor()));
        skillDTO.setStatus(skill.getStatus());
        skillDTO.setCreatedDate(skill.getCreatedDate());
        skillDTO.setModifiedDate(skill.getModifiedDate());
        skillDTO.setCreatedBy(skill.getCreatedBy());
        skillDTO.setModifiedBy(skill.getModifiedBy());
        return skillDTO;
    }
}
