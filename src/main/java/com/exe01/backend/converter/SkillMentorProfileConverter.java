package com.exe01.backend.converter;

import com.exe01.backend.dto.SkillMentorProfileDTO;
import com.exe01.backend.entity.SkillMentorProfile;

public class SkillMentorProfileConverter {

    public static SkillMentorProfileDTO toDto(SkillMentorProfile skillMentorProfile) {
        SkillMentorProfileDTO skillMentorProfileDTO = new SkillMentorProfileDTO();
        skillMentorProfileDTO.setSkill(SkillConverter.toDTO(skillMentorProfile.getSkill()));
        skillMentorProfileDTO.setSkillLevel(skillMentorProfile.getSkillLevel());
        return skillMentorProfileDTO;
    }

}
