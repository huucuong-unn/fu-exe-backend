package com.exe01.backend.dto.request.mentor;

import com.exe01.backend.dto.request.mentorProfile.CreateMentorProfileRequest;
import com.exe01.backend.dto.request.skillMentorProfile.BaseSkillMentorProfileRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateMentorRequest extends BaseMentorRequest{
    private CreateMentorProfileRequest mentorProfileRequest;
    List<String> skillNames;
}
