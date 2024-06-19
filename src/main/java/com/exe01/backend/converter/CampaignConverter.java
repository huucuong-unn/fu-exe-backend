package com.exe01.backend.converter;

import com.exe01.backend.dto.CampaignDTO;
import com.exe01.backend.entity.Campaign;

public class CampaignConverter {

    public static CampaignDTO toDto(Campaign campaign) {
        CampaignDTO campaignDTO = new CampaignDTO();
        campaignDTO.setId(campaign.getId());
        campaignDTO.setName(campaign.getName());
        campaignDTO.setImg(campaign.getImg());
        campaignDTO.setDescription(campaign.getDescription());
        campaignDTO.setStartDate(campaign.getStartDate());
        campaignDTO.setEndDate(campaign.getEndDate());
        campaignDTO.setCompanyApplyStartDate(campaign.getCompanyApplyStartDate());
        campaignDTO.setCompanyApplyEndDate(campaign.getCompanyApplyEndDate());
        campaignDTO.setMenteeApplyStartDate(campaign.getMenteeApplyStartDate());
        campaignDTO.setMenteeApplyEndDate(campaign.getMenteeApplyEndDate());
        campaignDTO.setTrainingStartDate(campaign.getTrainingStartDate());
        campaignDTO.setTrainingEndDate(campaign.getTrainingEndDate());
        campaignDTO.setCreatedDate(campaign.getCreatedDate());
        campaignDTO.setModifiedDate(campaign.getModifiedDate());
        campaignDTO.setCreatedBy(campaign.getCreatedBy());
        campaignDTO.setModifiedBy(campaign.getModifiedBy());
        campaignDTO.setStatus(campaign.getStatus());
        return campaignDTO;
    }

    public static Campaign toEntity(CampaignDTO campaignDTO) {
        Campaign campaign = new Campaign();
        campaign.setId(campaignDTO.getId());
        campaign.setImg(campaignDTO.getImg());
        campaign.setDescription(campaignDTO.getDescription());
        campaign.setName(campaignDTO.getName());
        campaign.setStartDate(campaignDTO.getStartDate());
        campaign.setEndDate(campaignDTO.getEndDate());
        campaign.setCompanyApplyStartDate(campaignDTO.getCompanyApplyStartDate());
        campaign.setCompanyApplyEndDate(campaignDTO.getCompanyApplyEndDate());
        campaign.setMenteeApplyStartDate(campaignDTO.getMenteeApplyStartDate());
        campaign.setMenteeApplyEndDate(campaignDTO.getMenteeApplyEndDate());
        campaign.setTrainingStartDate(campaignDTO.getTrainingStartDate());
        campaign.setTrainingEndDate(campaignDTO.getTrainingEndDate());
        campaign.setCreatedDate(campaignDTO.getCreatedDate());
        campaign.setModifiedDate(campaignDTO.getModifiedDate());
        campaign.setCreatedBy(campaignDTO.getCreatedBy());
        campaign.setModifiedBy(campaignDTO.getModifiedBy());
        campaign.setStatus(campaignDTO.getStatus());
        return campaign;
    }
}
