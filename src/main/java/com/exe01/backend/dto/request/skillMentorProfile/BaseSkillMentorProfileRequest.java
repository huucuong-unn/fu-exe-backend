package com.exe01.backend.dto.request.skillMentorProfile;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseSkillMentorProfileRequest {

    private UUID mentorProfileId;

    private UUID skillId;

    private String skillLevel;

}
