package com.exe01.backend.controller;

import com.exe01.backend.constant.ConstAPI;
import com.exe01.backend.dto.MenteeDTO;
import com.exe01.backend.dto.request.mentee.MenteeRequest;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.service.IMenteeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin
@Slf4j
@Tag(name = "Mentee Controller")
public class MenteeController {

    @Autowired
    private IMenteeService menteeService;

    @Operation(summary = "Get all mentee", description = "API get all mentee")
    @GetMapping(value = ConstAPI.MenteeAPI.GET_MENTEE)
    public PagingModel getALl(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Getting all mentee with page: {}, limit: {}", page, limit);
        return menteeService.getAll(page, limit);
    }

    @Operation(summary = "Get all mentee with status active", description = "API get all mentee with status active")
    @GetMapping(value = ConstAPI.MenteeAPI.GET_MENTEE_STATUS_TRUE)
    public PagingModel findAllWithStatusActive(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Getting all active mentee with page: {}, limit: {}", page, limit);
        return menteeService.findAllByStatusTrue(page, limit);
    }

    @Operation(summary = "Get mentee by id", description = "API get mentee by id")
    @GetMapping(value = ConstAPI.MenteeAPI.GET_MENTEE_BY_ID + "{id}")
    public MenteeDTO findById(@PathVariable("id") UUID id) throws BaseException {
        try {
            log.info("Getting mentee with id: {}", id);
            return menteeService.findById(id);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new BaseException(500, e.getMessage(), "Internal Server Error");
        }
    }

    @Operation(summary = "Create mentee", description = "API create new mentee")
    @PostMapping(value = ConstAPI.MenteeAPI.CREATE_MENTEE)
    public MenteeDTO create(@RequestBody MenteeRequest request) throws BaseException {
        log.info("Creating new mentee with request: {}", request);
        return menteeService.create(request);
    }

    @Operation(summary = "Delete mentee", description = "API delete mentee")
    @DeleteMapping(value = ConstAPI.MenteeAPI.CHANGE_STATUS_MENTEE + "{id}")
    public Boolean changStatus(@PathVariable("id") UUID id) throws BaseException {
        log.info("Deleting mentee with id: {}", id);
        return menteeService.changeStatus(id);
    }

//    @Operation(summary = "Get mentee by mentorId", description = "API  get mentee by mentorId")
//    @GetMapping(value = ConstAPI.MenteeAPI.GET_MENTEE_BY_MENTORID_CAMPAIGNID + "{id}")
//    public PagingModel findByMentorIdAndCampaignId(@PathVariable("id") UUID id) throws BaseException {
//        log.info("Deleting mentee with id: {}", id);
//        return menteeService.changeStatus(id);
//    }


}
