package com.exe01.backend.service;

import com.exe01.backend.dto.RoleDTO;
import com.exe01.backend.dto.request.role.CreateRoleRequest;
import com.exe01.backend.dto.request.role.UpdateRoleRequest;

import java.util.UUID;

public interface IRoleService extends IGenericService<RoleDTO>{
    RoleDTO create(CreateRoleRequest request);

    Boolean update(UUID id, UpdateRoleRequest request);

    Boolean delete(UUID id);

}
