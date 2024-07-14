package com.exe01.backend.dto.request.mentorProfile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateNewMentorProfileSkills {
    CreateMentorProfileRequest createMentorProfileRequest;
    List<String> skills;
}
