package com.exe01.backend.dto;

import com.exe01.backend.entity.MentorProfile;
import com.exe01.backend.entity.Skill;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SkillMentorProfileDTO {

    private MentorProfiledDTO mentorProfile;

    private Skill skill;

    private String skillLevel;

}
