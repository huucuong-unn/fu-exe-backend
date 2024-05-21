package com.exe01.backend.controller;

import com.exe01.backend.constant.ConstAPI;
import com.exe01.backend.dto.MentorProfileDTO;
import com.exe01.backend.dto.request.mentorProfile.CreateMentorProfileRequest;
import com.exe01.backend.dto.request.mentorProfile.UpdateMentorProfileRequest;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.service.IMentorProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin
public class MentorProfileController {

    @Autowired
    private IMentorProfileService mentorProfileService;

    @GetMapping(value = ConstAPI.MentorProfileAPI.GET_MENTOR_PROFILE)
    public PagingModel getALl(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        return mentorProfileService.getAll(page, limit);
    }

    @GetMapping(value = ConstAPI.MentorProfileAPI.GET_MENTOR_PROFILE_STATUS_TRUE)
    public PagingModel findAllWithStatusActive(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        return mentorProfileService.findAllByStatusTrue(page, limit);
    }

    @GetMapping(value = ConstAPI.MentorProfileAPI.GET_MENTOR_PROFILE_BY_ID + "{id}")
    public MentorProfileDTO findById(@PathVariable("id") UUID id) {
        return mentorProfileService.findById(id);
    }

    @PostMapping(value = ConstAPI.MentorProfileAPI.CREATE_MENTOR_PROFILE)
    public MentorProfileDTO create(@RequestBody CreateMentorProfileRequest request) {
        return mentorProfileService.create(request);
    }

    @PutMapping(value = ConstAPI.MentorProfileAPI.UPDATE_MENTOR_PROFILE + "{id}")
    public Boolean update(@PathVariable("id") UUID id, @RequestBody UpdateMentorProfileRequest request) {
        return mentorProfileService.update(id, request);
    }

    @DeleteMapping(value = ConstAPI.MentorProfileAPI.DELETE_MENTOR_PROFILE + "{id}")
    public Boolean delete(@PathVariable("id") UUID id) {
        return mentorProfileService.delete(id);
    }

}
