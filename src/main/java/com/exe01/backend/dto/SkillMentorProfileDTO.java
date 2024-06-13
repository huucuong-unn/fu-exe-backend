package com.exe01.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SkillMentorProfileDTO implements Serializable {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private MentorProfileDTO mentorProfile;

    private SkillDTO skill;

    private String skillLevel;

}
