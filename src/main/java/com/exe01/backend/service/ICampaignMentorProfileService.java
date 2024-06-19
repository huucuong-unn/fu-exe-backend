package com.exe01.backend.service;

import com.exe01.backend.dto.CampaignMentorProfileDTO;
import com.exe01.backend.dto.request.campaignMentorProfile.CreateCampaignMentorProfileRequest;
import com.exe01.backend.dto.request.campaignMentorProfile.UpdateCampaignMentorProfileRequest;
import com.exe01.backend.entity.Campaign;
import com.exe01.backend.exception.BaseException;

import java.util.UUID;

public interface ICampaignMentorProfileService{

    CampaignMentorProfileDTO findById(UUID id) throws BaseException;

    CampaignMentorProfileDTO create(CreateCampaignMentorProfileRequest request) throws BaseException;

    Boolean update(UUID id, UpdateCampaignMentorProfileRequest request) throws BaseException;

    Boolean changeStatus(UUID id) throws BaseException;

    CampaignMentorProfileDTO findByMentorIdAndStatus(UUID mentorId, String status) throws BaseException;

}
