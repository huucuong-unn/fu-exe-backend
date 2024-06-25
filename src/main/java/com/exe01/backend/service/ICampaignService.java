package com.exe01.backend.service;

import com.exe01.backend.dto.CampaignDTO;
import com.exe01.backend.dto.request.campaign.CreateCampaignRequest;
import com.exe01.backend.dto.request.campaign.UpdateCampaignRequest;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;

import java.util.List;
import java.util.UUID;

public interface ICampaignService extends IGenericService<CampaignDTO>{

    CampaignDTO create(CreateCampaignRequest request) throws BaseException;

    Boolean update(UUID id, UpdateCampaignRequest request) throws BaseException;

    Boolean changeStatus(UUID id) throws BaseException;

    List<CampaignDTO> findAll() throws BaseException;

    PagingModel findAllCampaignForAdminSearch(String campaignName, String status, int page, int size) throws BaseException;
}
