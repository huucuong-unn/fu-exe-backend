package com.exe01.backend.service;

import com.exe01.backend.dto.CampaignDTO;
import com.exe01.backend.dto.request.campaign.CreateCampaignRequest;
import com.exe01.backend.dto.request.campaign.UpdateCampaignRequest;
import com.exe01.backend.exception.BaseException;

import java.util.UUID;

public interface ICampaignService extends IGenericService<CampaignDTO>{

    CampaignDTO create(CreateCampaignRequest request) throws BaseException;

    Boolean update(UUID id, UpdateCampaignRequest request) throws BaseException;

    Boolean delete(UUID id) throws BaseException;

}
