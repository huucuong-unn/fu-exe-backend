package com.exe01.backend.dto.response.mentorProfile;

import com.exe01.backend.dto.*;
import com.exe01.backend.entity.Skill;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MentorsResponse implements Serializable {

    private MentorProfileDTO mentorProfile;

    private List<SkillMentorProfileDTO> skills;

}
