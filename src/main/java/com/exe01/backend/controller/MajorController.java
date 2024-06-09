package com.exe01.backend.controller;

import com.exe01.backend.constant.ConstAPI;
import com.exe01.backend.dto.MajorDTO;
import com.exe01.backend.dto.request.major.CreateMajorRequest;
import com.exe01.backend.dto.request.major.UpdateMajorRequest;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.service.IMajorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin
@Slf4j
@Tag(name = "Major Controller")
public class MajorController {

    @Autowired
    private IMajorService majorService;

    @Operation(summary = "Get all major", description = "API get all major")
    @GetMapping(value = ConstAPI.MajorAPI.GET_MAJOR)
    public PagingModel getALl(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Getting all majors with page: {}, limit: {}", page, limit);
        return majorService.getAll(page, limit);
    }

    @Operation(summary = "Get all major with status active", description = "API get all major with status active")
    @GetMapping(value = ConstAPI.MajorAPI.GET_MAJOR_STATUS_TRUE)
    public PagingModel findAllWithStatusActive(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Getting all active majors with page: {}, limit: {}", page, limit);
        return majorService.findAllByStatusTrue(page, limit);
    }

    @Operation(summary = "Get major by id", description = "API get major by id")
    @GetMapping(value = ConstAPI.MajorAPI.GET_MAJOR_BY_ID + "{id}")
    public MajorDTO findById(@PathVariable("id") UUID id) throws BaseException {
        try {
            log.info("Getting major with id: {}", id);
            return majorService.findById(id);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new BaseException(500, e.getMessage(), "Internal Server Error");
        }
    }

    @Operation(summary = "Create major", description = "API create new major")
    @PostMapping(value = ConstAPI.MajorAPI.CREATE_MAJOR)
    public MajorDTO create(@RequestBody CreateMajorRequest request) throws BaseException {
        log.info("Creating new major with request: {}", request);
        return majorService.create(request);
    }

    @Operation(summary = "Update major", description = "API update major")
    @PutMapping(value = ConstAPI.MajorAPI.UPDATE_MAJOR + "{id}")
    public Boolean update(@PathVariable("id") UUID id, @RequestBody UpdateMajorRequest request) throws BaseException {
        log.info("Updating major with id: {}, request: {}", id, request);
        return majorService.update(id, request);
    }

    @Operation(summary = "Delete major", description = "API delete major")
    @DeleteMapping(value = ConstAPI.MajorAPI.DELETE_MAJOR + "{id}")
    public Boolean delete(@PathVariable("id") UUID id) throws BaseException {
        log.info("Deleting major with id: {}", id);
        return majorService.delete(id);
    }

    @Operation(summary = "Change status major", description = "API change status major")
    @PutMapping(value = ConstAPI.MajorAPI.CHANGE_STATUS_MAJOR + "{id}")
    public Boolean changeStatus(@PathVariable("id") UUID id) throws BaseException {
        log.info("Change status account with id: {}", id);
        return majorService.changeStatus(id);
    }
}
