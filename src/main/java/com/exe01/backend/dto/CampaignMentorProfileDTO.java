package com.exe01.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CampaignMentorProfileDTO extends BaseDTO{

    private CampaignDTO campaign;

    private MentorProfileDTO mentorProfile;

}
