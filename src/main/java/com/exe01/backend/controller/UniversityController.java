package com.exe01.backend.controller;

import com.exe01.backend.constant.ConstAPI;
import com.exe01.backend.dto.UniversityDTO;
import com.exe01.backend.dto.request.university.CreateUniversityRequest;
import com.exe01.backend.dto.request.university.UpdateUniversityRequest;
import com.exe01.backend.dto.response.university.UniversityDropDownListResponse;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.service.IUniversityService;
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
@Tag(name = "University Controller")
public class UniversityController {

    @Autowired
    private IUniversityService universityService;

    @Operation(summary = "Get all university", description = "API get all university")
    @GetMapping(value = ConstAPI.UniversityAPI.GET_UNIVERSITY)
    public PagingModel getALl(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Getting all universities with page: {}, limit: {}", page, limit);
        return universityService.getAll(page, limit);
    }
    @Operation(summary = "Get all university for drop down list ", description = "API get all university for drop down list")
    @GetMapping(value = ConstAPI.UniversityAPI.GET_UNIVERSITY_DROP_DOWN_LIST)
    public List<UniversityDropDownListResponse> getALlDropDownList() throws BaseException {
        log.info("Getting all universities");
        return universityService.getAll();
    }

    @Operation(summary = "Get all university with status active", description = "API get all university with status active")
    @GetMapping(value = ConstAPI.UniversityAPI.GET_UNIVERSITY_STATUS_TRUE)
    public PagingModel findAllWithStatusActive(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Getting all active universities with page: {}, limit: {}", page, limit);
        return universityService.findAllByStatusTrue(page, limit);
    }

    @Operation(summary = "Get university by id", description = "API get university by id")
    @GetMapping(value = ConstAPI.UniversityAPI.GET_UNIVERSITY_BY_ID + "{id}")
    public UniversityDTO findById(@PathVariable("id") UUID id) throws BaseException {
        try {
            log.info("Getting university with id: {}", id);
            return universityService.findById(id);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new BaseException(500, e.getMessage(), "Internal Server Error");
        }
    }

    @Operation(summary = "Create university", description = "API create new university")
    @PostMapping(value = ConstAPI.UniversityAPI.CREATE_UNIVERSITY)
    public UniversityDTO create(@RequestBody CreateUniversityRequest request) throws BaseException {
        log.info("Creating new university with request: {}", request);
        return universityService.create(request);
    }

    @Operation(summary = "Update university", description = "API update university")
    @PutMapping(value = ConstAPI.UniversityAPI.UPDATE_UNIVERSITY + "{id}")
    public Boolean update(@PathVariable("id") UUID id, @RequestBody UpdateUniversityRequest request) throws BaseException {
        log.info("Updating university with id: {}, request: {}", id, request);
        return universityService.update(id, request);
    }

    @Operation(summary = "Delete university", description = "API delete university")
    @PutMapping(value = ConstAPI.UniversityAPI.CHANGE_STATUS_UNIVERSITY + "{id}")
    public Boolean changeStatus(@PathVariable("id") UUID id) throws BaseException {
        log.info("Deleting university with id: {}", id);
        return universityService.changeStatus(id);
    }

}
