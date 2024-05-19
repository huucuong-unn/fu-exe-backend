package com.exe01.backend.service.impl;

import com.exe01.backend.constant.ConstStatus;
import com.exe01.backend.converter.GenericConverter;
import com.exe01.backend.converter.RoleConverter;
import com.exe01.backend.dto.RoleDTO;
import com.exe01.backend.dto.request.role.CreateRoleRequest;
import com.exe01.backend.dto.request.role.UpdateRoleRequest;
import com.exe01.backend.entity.Role;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.repository.RoleRepository;
import com.exe01.backend.service.IRoleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    GenericConverter genericConverter;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public RoleDTO findById(UUID id) {
        boolean isExist = roleRepository.findById(id).isPresent();

        if (!isExist) {
            throw new EntityNotFoundException();
        }

        Role role = roleRepository.findById(id).get();

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        roleDTO.setDescription(role.getDescription());
        roleDTO.setStatus(role.getStatus());
        return roleDTO;
    }

    @Override
    public PagingModel getAll(Integer page, Integer limit) {
        PagingModel result = new PagingModel();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);

        List<Role> roles = roleRepository.findAllBy(pageable);

        List<RoleDTO> roleDTOS = roles.stream().map(RoleConverter::toDto).toList();

        result.setListResult(roleDTOS);

        result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
        result.setLimit(limit);

        return result;
    }

    public int totalItem() {
        return (int) roleRepository.count();
    }


    @Override
    public PagingModel findAllByStatusTrue(Integer page, Integer limit) {
        PagingModel result = new PagingModel();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);

        List<Role> roles = roleRepository.findAllByStatusTrueOOrderByCreatedDate(pageable);

        List<RoleDTO> roleDTOS = roles.stream().map(RoleConverter::toDto).toList();

        result.setListResult(roleDTOS);

        result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
        result.setLimit(limit);

        return result;
    }

    @Override
    public Boolean update(UUID id, UpdateRoleRequest request) {
        var roleById = roleRepository.findById(id);
        boolean isRole = roleById.isPresent();

        if (!isRole) {
            //TODO
            throw new EntityNotFoundException();
        }

        Role role = roleById.get();
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        role.setStatus(request.getStatus());

        try {
            roleRepository.save(role);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Failed to save role", ex);
        } catch (Exception ex) {
            throw new RuntimeException("An unexpected error occurred", ex);
        }

        return true;
    }

    @Override
    public RoleDTO create(CreateRoleRequest request) {
        Role role = new Role();
        RoleDTO roleDTO = (RoleDTO) genericConverter.toDTO(request, RoleDTO.class);
        role.setStatus(ConstStatus.ACTIVE_STATUS);
        role = RoleConverter.toEntity(roleDTO);

        roleRepository.save(role);

        return RoleConverter.toDto(role);
    }
}
