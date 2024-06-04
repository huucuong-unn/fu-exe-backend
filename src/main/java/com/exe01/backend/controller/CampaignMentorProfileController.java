package com.exe01.backend.controller;

import com.exe01.backend.constant.ConstAPI;
import com.exe01.backend.dto.CampaignMentorProfileDTO;
import com.exe01.backend.dto.request.campaignMentorProfile.CreateCampaignMentorProfileRequest;
import com.exe01.backend.dto.request.campaignMentorProfile.UpdateCampaignMentorProfileRequest;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.service.ICampaignMentorProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin
@RestController
@Tag(name = "Campaign Mentor Profile Controller")
@Slf4j
public class CampaignMentorProfileController {

    @Autowired
    private ICampaignMentorProfileService campaignMentorProfileService;

    // return CampaignMentorProfileDTO
    @Operation(summary = "Get campaign by id", description = "API get campagin mentor profile by id")
    @GetMapping(value = ConstAPI.CampaignMentorProfileAPI.GET_CAMPAIGN_MENTOR_PROFILE_BY_ID + "{id}", produces = "application/json")
    public CampaignMentorProfileDTO findById(@PathVariable("id") UUID id) throws BaseException {
        return campaignMentorProfileService.findById(id);
    }

    @Operation(summary = "Create campaign", description = "API create new campagin mentor profile")
    @PostMapping(value = ConstAPI.CampaignMentorProfileAPI.CREATE_CAMPAIGN_MENTOR_PROFILE)
    public CampaignMentorProfileDTO create(@RequestBody CreateCampaignMentorProfileRequest request) throws BaseException {
        log.info("Creating new campaign with request: {}", request);
        return campaignMentorProfileService.create(request);
    }

    @Operation(summary = "Update campaign", description = "API update campagin mentor profile")
    @PutMapping(value = ConstAPI.CampaignMentorProfileAPI.UPDATE_CAMPAIGN_MENTOR_PROFILE + "{id}")
    public Boolean update(@PathVariable("id") UUID id, @RequestBody UpdateCampaignMentorProfileRequest request) throws BaseException {
        log.info("Updating campaign with id: {}, request: {}", id, request);
        return campaignMentorProfileService.update(id, request);
    }

    @Operation(summary = "Change status campagin mentor profile", description = "API delete campagin mentor profile")
    @PatchMapping(value = ConstAPI.CampaignMentorProfileAPI.CHANGE_STATUS_CAMPAIGN_MENTOR_PROFILE + "{id}")
    public Boolean changeStatus(@PathVariable("id") UUID id) throws BaseException {
        log.info("Deleting university with id: {}", id);
        return campaignMentorProfileService.changeStatus(id);
    }

}
