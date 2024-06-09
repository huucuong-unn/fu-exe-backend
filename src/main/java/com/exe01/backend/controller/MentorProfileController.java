package com.exe01.backend.controller;

import com.exe01.backend.constant.ConstAPI;
import com.exe01.backend.dto.MentorProfileDTO;
import com.exe01.backend.dto.request.mentorProfile.CreateMentorProfileRequest;
import com.exe01.backend.dto.request.mentorProfile.UpdateMentorProfileRequest;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.service.IMentorProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "Update mentor profile", description = "API update mentor profile")
    @PutMapping(value = ConstAPI.MentorProfileAPI.UPDATE_MENTOR_PROFILE + "{id}")
    public Boolean update(@PathVariable("id") UUID id, @RequestBody UpdateMentorProfileRequest request) throws BaseException{
        log.info("Updating mentor profile with id: {}, request: {}", id, request);
        return mentorProfileService.update(id, request);
    }

    @Operation(summary = "Delete mentor profile", description = "API delete mentor profile")
    @DeleteMapping(value = ConstAPI.MentorProfileAPI.DELETE_MENTOR_PROFILE + "{id}")
    public Boolean delete(@PathVariable("id") UUID id)  throws BaseException{
        log.info("Deleting mentor profile with id: {}", id);
        return mentorProfileService.delete(id);
    }

}
