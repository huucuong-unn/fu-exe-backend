package com.exe01.backend.controller;

import com.exe01.backend.constant.ConstAPI;
import com.exe01.backend.dto.MentorProfileDTO;
import com.exe01.backend.dto.request.mentorProfile.CreateMentorProfileRequest;
import com.exe01.backend.dto.request.mentorProfile.CreateNewMentorProfileSkills;
import com.exe01.backend.dto.request.mentorProfile.UpdateMentorProfileRequest;
import com.exe01.backend.dto.response.mentorProfile.MentorsResponse;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.service.IMentorProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@Slf4j
@Tag(name = "Mentor Profile Controller")
public class MentorProfileController {

    @Autowired
    private IMentorProfileService mentorProfileService;

    @Operation(summary = "Get all mentor profile", description = "API get all mentor profile")
    @GetMapping(value = ConstAPI.MentorProfileAPI.GET_MENTOR_PROFILE)
    public PagingModel getALl(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Getting all mentor profiles with page: {}, limit: {}", page, limit);
        return mentorProfileService.getAll(page, limit);
    }

    @Operation(summary = "Get all mentor profile with status true", description = "API get all mentor profile with status true")
    @GetMapping(value = ConstAPI.MentorProfileAPI.GET_MENTOR_PROFILE_STATUS_TRUE)
    public PagingModel findAllWithStatusActive(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException{
        log.info("Getting all active mentor profiles with page: {}, limit: {}", page, limit);
        return mentorProfileService.findAllByStatusTrue(page, limit);
    }

    @Operation(summary = "Get mentor profile by id", description = "API get mentor profile by id")
    @GetMapping(value = ConstAPI.MentorProfileAPI.GET_MENTOR_PROFILE_BY_ID + "{id}")
    public MentorProfileDTO findById(@PathVariable("id") UUID id) throws BaseException {
        try {
            log.info("Getting mentor profile with id: {}", id);
            return mentorProfileService.findById(id);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new BaseException(500, e.getMessage(), "Internal Server Error");
        }
    }

    @Operation(summary = "Create mentor profile", description = "API create new mentor profile")
    @PostMapping(value = ConstAPI.MentorProfileAPI.CREATE_MENTOR_PROFILE)
    public MentorProfileDTO create(@RequestBody CreateMentorProfileRequest request) throws BaseException{
        log.info("Creating new mentor profile with request: {}", request);
        return mentorProfileService.create(request);
    }
    @Operation(summary = "Create new mentor profile skill", description = "API Create new mentor profile skill")
    @PostMapping(value = ConstAPI.MentorProfileAPI.CREATE_NEW_MENTOR_PROFILE_SKILLS)
    public void createNewMentorSkillProfile(@RequestBody CreateNewMentorProfileSkills createMentorProfileRequest) throws BaseException{
        log.info("Creating new mentor profile skill with request: {}", createMentorProfileRequest);
        mentorProfileService.createNewMentorSkillProfile(createMentorProfileRequest.getCreateMentorProfileRequest(), createMentorProfileRequest.getSkills());
    }

    @Operation(summary = "Update mentor profile", description = "API update mentor profile")
    @PutMapping(value = ConstAPI.MentorProfileAPI.UPDATE_MENTOR_PROFILE)
    public void update(@RequestBody CreateNewMentorProfileSkills request) throws BaseException{
        log.info("Updating mentor profile with id: {}, request: {}", request.getCreateMentorProfileRequest().getMentorId());
         mentorProfileService.updateMentorSkillProfile(request.getCreateMentorProfileRequest(), request.getSkills());
    }

    @Operation(summary = "Delete mentor profile", description = "API delete mentor profile")
    @DeleteMapping(value = ConstAPI.MentorProfileAPI.DELETE_MENTOR_PROFILE + "{id}")
    public Boolean delete(@PathVariable("id") UUID id)  throws BaseException{
        log.info("Deleting mentor profile with id: {}", id);
        return mentorProfileService.delete(id);
    }

    @Operation(summary = "Find All By MentorId", description = "API Find All By MentorId")
    @GetMapping(value = ConstAPI.MentorProfileAPI.GET_ALL_MENTOR_PROFILE_BY_MENTOR_ID + "{id}")
    public List<MentorsResponse> findAllByMentorId(@PathVariable("id") UUID id) throws BaseException{
        log.info("Finding all mentor profiles by mentor id: {}", id);
        return mentorProfileService.findAllByMentorId(id);
    }

    @Operation(summary = "Find Mentor Profile Using By MentorId", description = "API Find Mentor Profile Using By MentorId")
    @GetMapping(value = ConstAPI.MentorProfileAPI.GET_MENTOR_PROFILE_BY_MENTOR_ID + "{id}")
    public MentorsResponse findMentorProfileUsingByMentorId(@PathVariable("id") UUID id) throws BaseException{
        log.info("Finding mentor profile by mentor id: {}", id);
        return mentorProfileService.findMentorProfileUsingByMentorId(id);
    }

}
