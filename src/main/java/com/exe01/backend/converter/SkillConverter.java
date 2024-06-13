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

    public static Skill toEntity(SkillDTO skillDTO) {
        Skill skill = new Skill();
        skill.setId(skillDTO.getId());
        skill.setName(skillDTO.getName());
        skill.setMajor(MajorConverter.toEntity(skillDTO.getMajor()));
        skill.setStatus(skillDTO.getStatus());
        skill.setCreatedDate(skillDTO.getCreatedDate());
        skill.setModifiedDate(skillDTO.getModifiedDate());
        skill.setCreatedBy(skillDTO.getCreatedBy());
        skill.setModifiedBy(skillDTO.getModifiedBy());
        return skill;
    }
}
