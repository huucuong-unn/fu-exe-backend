package com.exe01.backend.controller;

import com.exe01.backend.constant.ConstAPI;
import com.exe01.backend.dto.SkillDTO;
import com.exe01.backend.dto.SkillMentorProfileDTO;
import com.exe01.backend.dto.request.skill.CreateSkillRequest;
import com.exe01.backend.dto.request.skill.UpdateSkillRequest;
import com.exe01.backend.dto.request.skillMentorProfile.BaseSkillMentorProfileRequest;
import com.exe01.backend.dto.response.skill.AllSkillOfCompanyResponse;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.service.ISkillMentorProfileService;
import com.exe01.backend.service.ISkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@Tag(name = "Skill Controller")
@Slf4j
public class SkillController {

    @Autowired
    private ISkillService skillService;

    @Autowired
    private ISkillMentorProfileService skillMentorProfileService;

    @Operation(summary = "Get all skill", description = "API get all skill")
    @GetMapping(value = ConstAPI.SkillAPI.GET_SKILL)
    public PagingModel getAll(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Getting all skill with page: {}, limit: {}", page, limit);
        return skillService.getAll(page, limit);
    }

    @Operation(summary = "Get all skill with status active", description = "API get all skill with status active")
    @GetMapping(value = ConstAPI.SkillAPI.GET_SKILL_STATUS_TRUE)
    public PagingModel getAllWithStatusTrue(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Getting all roles with status true with page: {}, limit: {}", page, limit);
        return skillService.findAllByStatusTrue(page, limit);
    }

    @Operation(summary = "Get skill by id", description = "API get skill by id")
    @GetMapping(value = ConstAPI.SkillAPI.GET_SKILL_BY_ID + "{id}")
    public SkillDTO findById(@PathVariable("id") UUID id) throws BaseException {
        try {
            log.info("Getting skill with id: {}", id);
            return skillService.findById(id);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new BaseException(500, e.getMessage(), "Internal Server Error");
        }
    }

    @Operation(summary = "Get all skill by company id", description = "API get all skill by company id")
    @GetMapping(value = ConstAPI.SkillAPI.GET_ALL_SKILL_BY_COMPANY_ID + "{id}")
    public List<AllSkillOfCompanyResponse> findALLByCompanyId(@PathVariable("id") UUID id) throws BaseException {
            log.info("Getting all skill with company id: {}", id);
            return skillService.getAllSkillOfCompany(id);
    }

    @Operation(summary = "Create skill", description = "API create new skill")
    @PostMapping(value = ConstAPI.SkillAPI.CREATE_SKILL)
    public SkillDTO create(@RequestBody CreateSkillRequest request) throws BaseException {
        log.info("Creating new skill with request: {}", request);
        return skillService.create(request);
    }

    @Operation(summary = "Create skill", description = "API create new skill mentor profile")
    @PostMapping(value = ConstAPI.SkillAPI.CREATE_SKILL_MENTOR_PROFILE)
    public SkillMentorProfileDTO createSkillMentorProfile(@RequestBody BaseSkillMentorProfileRequest request) throws BaseException {
        log.info("Creating new skill mentor profile with request: {}", request);
        return skillMentorProfileService.create(request);
    }

    @Operation(summary = "Update skill", description = "API update skill")
    @PutMapping(value = ConstAPI.SkillAPI.UPDATE_SKILL + "{id}")
    public Boolean update(@PathVariable("id") UUID id, @RequestBody UpdateSkillRequest request) throws BaseException {
        log.info("Updating role with id: {}, request: {}", id, request);
        return skillService.update(id, request);
    }

    @Operation(summary = "Delete skill", description = "API delete skill")
    @DeleteMapping(value = ConstAPI.SkillAPI.DELETE_SKILL + "{id}")
    public Boolean delete(@PathVariable("id") UUID id) throws BaseException {
        log.info("Deleting skill with id: {}", id);
        return skillService.delete(id);
    }

    @Operation(summary = "Change status skill", description = "API change status skill")
    @PutMapping(value = ConstAPI.SkillAPI.CHANGE_STATUS_SKILL + "{id}")
    public Boolean changeStatus(@PathVariable("id") UUID id) throws BaseException {
        log.info("Change status skill with id: {}", id);
        return skillService.changeStatus(id);
    }

}
