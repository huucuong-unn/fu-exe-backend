package com.exe01.backend.controller;

import com.exe01.backend.constant.ConstAPI;
import com.exe01.backend.dto.RoleDTO;
import com.exe01.backend.dto.request.role.CreateRoleRequest;
import com.exe01.backend.dto.request.role.UpdateRoleRequest;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.service.IRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin
@RestController
@Tag(name = "Role Controller")
@Slf4j
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @Operation(summary = "Get all role", description = "API get all role")
    @GetMapping(value = ConstAPI.RoleAPI.GET_ROLE)
    public PagingModel getAll(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Getting all roles with page: {}, limit: {}", page, limit);
        return roleService.getAll(page, limit);
    }

    @Operation(summary = "Get all role with status active", description = "API get all role with status active")
    @GetMapping(value = ConstAPI.RoleAPI.GET_ROLE_STATUS_TRUE)
    public PagingModel getAllWithStatusTrue(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Getting all roles with status true with page: {}, limit: {}", page, limit);
        return roleService.findAllByStatusTrue(page, limit);
    }

    @Operation(summary = "Get role by id", description = "API get role by id")
    @GetMapping(value = ConstAPI.RoleAPI.GET_ROLE_BY_ID + "{id}")
    public RoleDTO findById(@PathVariable("id") UUID id) throws BaseException {
        try {
            log.info("Getting role with id: {}", id);
            return roleService.findById(id);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new BaseException(500, e.getMessage(), "Internal Server Error");
        }
    }

    @Operation(summary = "Create role", description = "API create new role")
    @PostMapping(value = ConstAPI.RoleAPI.CREATE_ROLE)
    public RoleDTO create(@RequestBody CreateRoleRequest request) throws BaseException {
        log.info("Creating new role with request: {}", request);
        return roleService.create(request);
    }

    @Operation(summary = "Update role", description = "API update role")
    @PutMapping(value = ConstAPI.RoleAPI.UPDATE_ROLE + "{id}")
    public Boolean update(@PathVariable("id") UUID id, @RequestBody UpdateRoleRequest request) throws BaseException {
        log.info("Updating role with id: {}, request: {}", id, request);
        return roleService.update(id, request);
    }

    @Operation(summary = "Delete role", description = "API delete role")
    @DeleteMapping(value = ConstAPI.RoleAPI.DELETE_ROLE + "{id}")
    public Boolean delete(@PathVariable("id") UUID id) throws BaseException {
        log.info("Deleting role with id: {}", id);
        return roleService.delete(id);
    }

    @Operation(summary = "Change status role", description = "API change status role")
    @PutMapping(value = ConstAPI.RoleAPI.CHANGE_STATUS_ROLE + "{id}")
    public Boolean changeStatus(@PathVariable("id") UUID id) throws BaseException {
        log.info("Change status role with id: {}", id);
        return roleService.changeStatus(id);
    }
}
