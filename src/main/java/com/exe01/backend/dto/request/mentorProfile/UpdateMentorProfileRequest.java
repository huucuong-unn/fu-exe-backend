package com.exe01.backend.dto.request.mentorProfile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMentorProfileRequest extends BaseMentorProfileRequest{
    private String status;
}
