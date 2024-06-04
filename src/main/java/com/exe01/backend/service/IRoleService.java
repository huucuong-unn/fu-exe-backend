package com.exe01.backend.service;

import com.exe01.backend.dto.RoleDTO;
import com.exe01.backend.dto.request.role.CreateRoleRequest;
import com.exe01.backend.dto.request.role.UpdateRoleRequest;
import com.exe01.backend.exception.BaseException;

import java.util.UUID;

public interface IRoleService extends IGenericService<RoleDTO>{
    RoleDTO create(CreateRoleRequest request) throws BaseException;

    Boolean update(UUID id, UpdateRoleRequest request) throws BaseException;

    Boolean delete(UUID id) throws BaseException;

    Boolean changeStatus(UUID id) throws BaseException;

    RoleDTO findByName(String name) throws BaseException;


}
