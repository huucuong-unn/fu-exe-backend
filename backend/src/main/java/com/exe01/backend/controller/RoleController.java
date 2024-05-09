package com.exe01.backend.controller;

import com.exe01.backend.constant.ConstAPI;
import com.exe01.backend.dto.RoleDTO;
import com.exe01.backend.dto.request.role.CreateRoleRequest;
import com.exe01.backend.dto.request.role.UpdateRoleRequest;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class RoleController {
    @Autowired
    private IRoleService roleService;

    @GetMapping(value = ConstAPI.AuthenticationAPI.ROLE)
    public PagingModel findAllOrderByIdWithPaging(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        return roleService.findAllWithPaging(page, limit);
    }

    @GetMapping(value = ConstAPI.AuthenticationAPI.ROLE_WITH_STATUS_TRUE)
    public PagingModel findAllByStatusTrueWithPaging(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        return roleService.findAllByStatusTrue(page, limit);
    }

    @PostMapping(value = ConstAPI.AuthenticationAPI.CREATE_ROLE)
    public RoleDTO create(@RequestBody CreateRoleRequest request) {
        return roleService.create(request);
    }

    @PutMapping(value = ConstAPI.AuthenticationAPI.UPDATE_ROLE + "{id}")
    public RoleDTO update(@RequestBody UpdateRoleRequest request, @PathVariable("id") Long id) {
        return roleService.update(request, id);
    }

    @DeleteMapping(value = ConstAPI.AuthenticationAPI.DELETE_ROLE + "{id}")
    public Boolean delete(@PathVariable Long id) {
        return roleService.delete(id);
    }
}
