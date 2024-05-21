package com.exe01.backend.dto;

import com.exe01.backend.entity.Skill;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SkillMentorProfileDTO {

    private MentorProfileDTO mentorProfile;

    private Skill skill;

    private String skillLevel;

}
