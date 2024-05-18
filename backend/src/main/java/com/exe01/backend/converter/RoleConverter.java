package com.exe01.backend.converter;

import com.exe01.backend.dto.RoleDTO;
import com.exe01.backend.entity.Role;

public class RoleConverter {
    public static RoleDTO toDto(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        roleDTO.setDescription(role.getDescription());
        roleDTO.setStatus(role.getStatus());
        roleDTO.setCreateBy(role.getCreateBy());
        roleDTO.setModifiedDate(role.getModifiedDate());
        roleDTO.setModifiedBy(role.getModifiedBy());
        roleDTO.setCreateBy(role.getCreateBy());

        return roleDTO;
    }
}
