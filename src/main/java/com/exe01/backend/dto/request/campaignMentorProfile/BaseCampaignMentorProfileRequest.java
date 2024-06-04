package com.exe01.backend.dto.request.campaignMentorProfile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseCampaignMentorProfileRequest {
    private UUID campaignId;
    private UUID mentorProfileId;
}
