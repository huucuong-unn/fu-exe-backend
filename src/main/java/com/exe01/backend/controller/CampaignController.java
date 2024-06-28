package com.exe01.backend.controller;

import com.exe01.backend.constant.ConstAPI;
import com.exe01.backend.dto.CampaignDTO;
import com.exe01.backend.dto.request.campaign.CreateCampaignRequest;
import com.exe01.backend.dto.request.campaign.UpdateCampaignRequest;
import com.exe01.backend.entity.Campaign;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.service.ICampaignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@Tag(name = "Campaign Controller")
@Slf4j
public class CampaignController {
    @Autowired
    private ICampaignService campaignService;

    @Operation(summary = "Get all campaign", description = "API get all campaign")
    @GetMapping(value = ConstAPI.CampaignAPI.GET_CAMPAIGN)
    public PagingModel getAll(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Getting all campaigns with page: {}, limit: {}", page, limit);
        return campaignService.getAll(page, limit);
    }

    @Operation(summary = "Get all campaign with status active", description = "API get all role with status active")
    @GetMapping(value = ConstAPI.CampaignAPI.GET_CAMPAIGN_STATUS_TRUE)
    public PagingModel getAllWithStatusTrue(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException{
        log.info("Getting all campaigns with status true with page: {}, limit: {}", page, limit);
        return campaignService.findAllByStatusTrue(page, limit);
    }

    // return campaignDTO
    @Operation(summary = "Get campaign by id", description = "API get campaign by id")
    @GetMapping(value = ConstAPI.CampaignAPI.GET_CAMPAIGN_BY_ID + "{id}",produces = "application/json")
    public CampaignDTO findById(@PathVariable("id") UUID id) throws BaseException {
            return campaignService.findById(id);

    }

    @Operation(summary = "Create campaign", description = "API create new campaign")
    @PostMapping(value = ConstAPI.CampaignAPI.CREATE_CAMPAIGN, consumes = MediaType.MULTIPART_FORM_DATA_VALUE ,produces = MediaType.APPLICATION_JSON_VALUE)
    public CampaignDTO create(@ModelAttribute CreateCampaignRequest request) throws BaseException{
        log.info("Creating new campaign with request: {}", request);
        return campaignService.create(request);
    }

    @Operation(summary = "Update campaign", description = "API update campaign")
    @PutMapping(value = ConstAPI.CampaignAPI.UPDATE_CAMPAIGN + "{id}")
    public Boolean update(@PathVariable("id") UUID id, @RequestBody UpdateCampaignRequest request) throws BaseException {
        log.info("Updating campaign with id: {}, request: {}", id, request);
        return campaignService.update(id, request);
    }

    @Operation(summary = "Delete campagin", description = "API delete campagin")
    @DeleteMapping(value = ConstAPI.CampaignAPI.CHANGE_STATUS_CAMPAIGN + "{id}")
    public Boolean delete(@PathVariable("id") UUID id) throws BaseException{
        log.info("Deleting university with id: {}", id);
        return campaignService.changeStatus(id);
    }

    @Operation(summary = "Get all campaign without paging", description = "API get all campaign without paging")
    @GetMapping(value = ConstAPI.CampaignAPI.GET_ALL_CAMPAIGN_WITHOUT_PAGING)
    public List<CampaignDTO> getAll() throws BaseException {
        log.info("Getting all campaigns without paging");
        return campaignService.findAll();
    }

    @Operation(summary = "Get all campaign with search", description = "API get all campaign with search")
    @GetMapping(value = ConstAPI.CampaignAPI.GET_CAMPAIGN_WITH_SEARCH)
    public PagingModel getAllWithSearch(@RequestParam(value = "campaignName", required = false) String campaignName, @RequestParam(value = "status", required = false) String status, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Getting all campaigns with search with campaignName: {}, status: {}, page: {}, limit: {}", campaignName, status, page, limit);
        return campaignService.findAllCampaignForAdminSearch(campaignName, status, page, limit);
    }

}
