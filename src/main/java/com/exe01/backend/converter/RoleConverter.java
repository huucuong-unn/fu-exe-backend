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
        roleDTO.setCreatedDate(role.getCreatedDate());
        roleDTO.setModifiedDate(role.getModifiedDate());
        roleDTO.setModifiedBy(role.getModifiedBy());
        roleDTO.setCreatedBy(role.getCreatedBy());

        return roleDTO;

    }

    public static Role toEntity(RoleDTO roleDTO) {

        Role role = new Role();
        role.setId(roleDTO.getId());
        role.setName(roleDTO.getName());
        role.setDescription(roleDTO.getDescription());
        role.setStatus(roleDTO.getStatus());
        role.setCreatedDate(roleDTO.getCreatedDate());
        role.setModifiedDate(roleDTO.getModifiedDate());
        role.setModifiedBy(roleDTO.getModifiedBy());
        role.setCreatedBy(roleDTO.getCreatedBy());

        return role;
    }

}
