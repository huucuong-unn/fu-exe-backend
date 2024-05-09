package com.exe01.backend.service;

import com.exe01.backend.dto.RoleDTO;
import com.exe01.backend.dto.request.role.CreateRoleRequest;
import com.exe01.backend.dto.request.role.UpdateRoleRequest;
import com.exe01.backend.models.PagingModel;

public interface IRoleService extends IGenericService<RoleDTO> {
    PagingModel findAllWithPaging(Integer page, Integer limit);

    RoleDTO create(CreateRoleRequest request);

    RoleDTO update(UpdateRoleRequest request, Long id);

    Boolean delete(Long id);

    PagingModel findAllByStatusTrue(Integer page, Integer limit);
}
