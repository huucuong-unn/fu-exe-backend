package com.exe01.backend.controller;

import com.exe01.backend.constant.ConstAPI;
import com.exe01.backend.dto.MentorApplyDTO;
import com.exe01.backend.dto.request.mentorApply.BaseMentorApplyRequest;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.service.IMentorApplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin
@Tag(name = "MenteeApply Controller")
public class MentorApplyController {

    @Autowired
    private IMentorApplyService menteeApplyService;

    @Operation(summary = "Get mentor application by id", description = "API get mentor application by id")
    @GetMapping(value = ConstAPI.MentorApplyAPI.GET_MENTOR_APPLY_BY_ID + "{id}")
    public MentorApplyDTO findById(@PathVariable("id") UUID id) throws BaseException {
        return menteeApplyService.findById(id);
    }

    @Operation(summary = "Create mentee application", description = "API create new mentee application")
    @PostMapping(value = ConstAPI.MentorApplyAPI.CREATE_MENTOR_APPLY)
    public MentorApplyDTO create(@RequestBody BaseMentorApplyRequest request) throws BaseException {
        return menteeApplyService.create(request);
    }

    @Operation(summary = "Update mentee application", description = "API update mentee application")
    @PutMapping(value = ConstAPI.MentorApplyAPI.UPDATE_MENTOR_APPLY + "{id}")
    public Boolean update(@PathVariable("id") UUID id, @RequestBody BaseMentorApplyRequest request) throws BaseException {
        return menteeApplyService.update(id, request);
    }

    @Operation(summary = "Change status of mentee application", description = "API change status of mentee application")
    @PutMapping(value = ConstAPI.MentorApplyAPI.CHANGE_STATUS_MENTOR_APPLY + "{id}")
    public Boolean changeStatus(@PathVariable("id") UUID id) throws BaseException {
        return menteeApplyService.changeStatus(id);
    }

    @Operation(summary = "Get mentee applications by application mentor id", description = "API get mentee applications by application mentor id")
    @GetMapping(value = ConstAPI.MentorApplyAPI.GET_MENTOR_APPLY_BY_MENTOR_ID + "{mentorId}")
    public PagingModel findByApplicationMentorId(@PathVariable("mentorId") UUID mentorId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        return menteeApplyService.findByApplicationMentorId(mentorId, page, limit);
    }

    @Operation(summary = "Get mentee applications by mentee id", description = "API get mentee applications by mentee id")
    @GetMapping(value = ConstAPI.MentorApplyAPI.GET_MENTOR_APPLY_BY_MENTEE_ID + "{menteeId}")
    public PagingModel findByMenteeId(@PathVariable("menteeId") UUID menteeId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        return menteeApplyService.findByMenteeId(menteeId, page, limit);
    }

    @Operation(summary = "Get mentee applications by mentee name, mentor full name and campaign id", description = "API get mentee applications by mentee name, mentor full name and campaign id")
    @GetMapping(value = ConstAPI.MentorApplyAPI.GET_MENTOR_APPLY_BY_MENTEE_NAME_AND_MENTOR_FULL_NAME_AND_CAMPAIGN_ID)
    public PagingModel findAllByMenteeNameAndMentorFullNameAndCampaignId(@RequestParam(value = "menteeName", required = false) String menteeName,
                                                                         @RequestParam(value = "mentorFullName", required = false) String mentorFullName,
                                                                         @RequestParam(value = "campaignId", required = false) UUID campaignId,
                                                                         @RequestParam(value = "companyId", required = false) UUID companyId,
                                                                         @RequestParam(value = "page", required = false) Integer page,
                                                                         @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        return menteeApplyService.findAllByMenteeNameAndMentorFullNameAndCampaignIdAndCompanyId(menteeName, mentorFullName, campaignId, companyId,page, limit);
    }

}