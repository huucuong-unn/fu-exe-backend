package com.exe01.backend.converter;

import com.exe01.backend.constant.ConstError;
import com.exe01.backend.dto.CampaignMentorProfileDTO;
import com.exe01.backend.entity.CampaignMentorProfile;
import com.exe01.backend.service.IMentorProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CampaignMentorProfileConverter {
    public static CampaignMentorProfileDTO toDto(CampaignMentorProfile campaignMentorProfile){
        CampaignMentorProfileDTO campaignMentorProfileDTO = new CampaignMentorProfileDTO();
        campaignMentorProfileDTO.setId(campaignMentorProfile.getId());
        campaignMentorProfileDTO.setMentorProfile(MentorProfileConverter.toDto(campaignMentorProfile.getMentorProfile()));
        campaignMentorProfileDTO.setCampaign(CampaignConverter.toDto(campaignMentorProfile.getCampaign()));
        campaignMentorProfileDTO.setCreatedDate(campaignMentorProfile.getCreatedDate());
        campaignMentorProfileDTO.setModifiedDate(campaignMentorProfile.getModifiedDate());
        campaignMentorProfileDTO.setCreatedBy(campaignMentorProfile.getCreatedBy());
        campaignMentorProfileDTO.setModifiedBy(campaignMentorProfile.getModifiedBy());
        return campaignMentorProfileDTO;
    }

    public static CampaignMentorProfile toEntity(CampaignMentorProfileDTO campaignMentorProfileDTO) {
        CampaignMentorProfile campaignMentorProfile = new CampaignMentorProfile();
        campaignMentorProfile.setId(campaignMentorProfileDTO.getId());
        campaignMentorProfile.setMentorProfile(MentorProfileConverter.toEntity(campaignMentorProfileDTO.getMentorProfile()));
        campaignMentorProfile.setCampaign(CampaignConverter.toEntity(campaignMentorProfileDTO.getCampaign()));
        campaignMentorProfile.setCreatedDate(campaignMentorProfileDTO.getCreatedDate());
        campaignMentorProfile.setModifiedDate(campaignMentorProfileDTO.getModifiedDate());
        campaignMentorProfile.setCreatedBy(campaignMentorProfileDTO.getCreatedBy());
        campaignMentorProfile.setModifiedBy(campaignMentorProfileDTO.getModifiedBy());
        return campaignMentorProfile;
    }


}
