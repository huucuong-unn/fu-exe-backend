package com.exe01.backend.controller;

import com.exe01.backend.constant.ConstAPI;
import com.exe01.backend.dto.ApplicationDTO;
import com.exe01.backend.dto.request.application.BaseApplicationRequest;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.service.IApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin
@Slf4j
@Tag(name = "Application Controller")
public class ApplicationController {

    @Autowired
    private IApplicationService applicationService;

    @Operation(summary = "Get all applications", description = "API get all applications")
    @GetMapping(value = ConstAPI.ApplicationAPI.GET_APPLICATION)
    public PagingModel getAll(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Getting all applications with page: {}, limit: {}", page, limit);
        return applicationService.getAll(page, limit);
    }

    @Operation(summary = "Get application by id", description = "API get application by id")
    @GetMapping(value = ConstAPI.ApplicationAPI.GET_APPLICATION_BY_ID + "{id}")
    public ApplicationDTO findById(@PathVariable("id") UUID id) throws BaseException {
        log.info("Getting application with id: {}", id);
        return applicationService.findById(id);
    }

    @Operation(summary = "Create application", description = "API create new application")
    @PostMapping(value = ConstAPI.ApplicationAPI.CREATE_APPLICATION,consumes = MediaType.MULTIPART_FORM_DATA_VALUE ,produces = MediaType.APPLICATION_JSON_VALUE)
    public ApplicationDTO create(@ModelAttribute  BaseApplicationRequest request) throws BaseException {
        log.info("Creating new application with request: {}", request);
        return applicationService.create(request);
    }

    @Operation(summary = "Update application", description = "API update application")
    @PutMapping(value = ConstAPI.ApplicationAPI.UPDATE_APPLICATION + "{id}")
    public Boolean update(@PathVariable("id") UUID id, @RequestBody BaseApplicationRequest request) throws BaseException {
        log.info("Updating application with id: {}, request: {}", id, request);
        return applicationService.update(id, request);
    }

    @Operation(summary = "Change status of application", description = "API change status of application")
    @PutMapping(value = ConstAPI.ApplicationAPI.CHANGE_STATUS_APPLICATION + "{id}")
    public Boolean changeStatus(@PathVariable("id") UUID id) throws BaseException {
        log.info("Changing status of application with id: {}", id);
        return applicationService.changeStatus(id);
    }

    @Operation(summary = "Get applications by mentor id", description = "API get applications by mentor id")
    @GetMapping(value = ConstAPI.ApplicationAPI.GET_APPLICATION_BY_MENTOR_ID + "{mentorId}")
    public PagingModel findByMentorId(@PathVariable("mentorId") UUID mentorId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Getting applications with mentor id: {}, page: {}, limit: {}", mentorId, page, limit);
        return applicationService.findByMentorId(mentorId, page, limit);
    }

    @Operation(summary = "Get applications by mentee id", description = "API get applications by mentee id")
    @GetMapping(value = ConstAPI.ApplicationAPI.GET_APPLICATION_BY_MENTEE_ID + "{menteeId}")
    public PagingModel findByMenteeId(@PathVariable("menteeId") UUID menteeId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Getting applications with mentee id: {}, page: {}, limit: {}", menteeId, page, limit);
        return applicationService.findByMenteeId(menteeId, page, limit);
    }

    @Operation(summary = "Approve Application", description = "API approve application")
    @PostMapping(value = ConstAPI.ApplicationAPI.APPROVE_APPLICATION + "{applicationId}")
    public void approveApplication(@PathVariable("applicationId") UUID applicationId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Approve application with application id: {}", applicationId);
        applicationService.approveApplication(applicationId);
    }

    @Operation(summary = "Get applications by mentor id and status and sort by created date", description = "API get applications by mentor id and status and sort by created date")
    @GetMapping(value = ConstAPI.ApplicationAPI.GET_APPLICATION_BY_MENTOR_ID_AND_STATUS_AND_SORT_BY_CREATED_DATE + "{mentorId}")
    public PagingModel findByMentorIdAndStatusAndSortByCreatedDate(@PathVariable("mentorId") UUID mentorId, @RequestParam(value = "status", required = false) String status, @RequestParam(value = "campaignStatus", required = false) String campaignStatus, @RequestParam(value = "createdDate", required = false) String createdDate, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Getting applications with mentor id: {}, status: {}, campaignStatus: {}, createdDate: {}, page: {}, limit: {}", mentorId, status, createdDate, page, limit);
        return applicationService.findByMentorIdAndStatusAndSortByCreatedDate(mentorId, status, createdDate, page, limit);
    }

    @Operation(summary = "Get applications by student id and status and sort", description = "API get applications by student id and status and sort")
    @GetMapping(value = ConstAPI.ApplicationAPI.GET_APPLICATION_BY_STUDENT_ID_AND_STATUS_AND_SORT+"{studentId}")
    public PagingModel findByStudentIdAndStatusAndSort(@PathVariable(value = "studentId", required = false) UUID studentId, @RequestParam(value = "companyId", required = false) UUID companyId, @RequestParam(value = "mentorName", required = false) String mentorName, @RequestParam(value = "status", required = false) String status, @RequestParam(value = "createdDate", required = false) String createdDate, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        return applicationService.findByStudentIdAndStatusAndSort(studentId, companyId, mentorName, status, createdDate, page, limit);

    }

}