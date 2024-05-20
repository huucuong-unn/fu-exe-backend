package com.exe01.backend.service.impl;

import com.exe01.backend.converter.RoleConverter;
import com.exe01.backend.constant.ConstStatus;
import com.exe01.backend.converter.GenericConverter;
import com.exe01.backend.dto.RoleDTO;
import com.exe01.backend.dto.request.role.CreateRoleRequest;
import com.exe01.backend.dto.request.role.UpdateRoleRequest;
import com.exe01.backend.entity.Role;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.repository.RoleRepository;
import com.exe01.backend.service.IRoleService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoleServiceImpl implements IRoleService {

    Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    GenericConverter genericConverter;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public RoleDTO findById(UUID id) {
        logger.info("Find Role by id {}", id);
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
        logger.info("Get all Role with paging");
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
        logger.info("Get all Role with status active");
        PagingModel result = new PagingModel();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);

        List<Role> roles = roleRepository.findAllByStatusOrderByCreatedDate(ConstStatus.ACTIVE_STATUS,pageable);

        List<RoleDTO> roleDTOS = roles.stream().map(RoleConverter::toDto).toList();

        result.setListResult(roleDTOS);

        result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
        result.setLimit(limit);

        return result;
    }

    @Override
    public Boolean update(UUID id, UpdateRoleRequest request) {
        logger.info("Update role");
        var roleById = roleRepository.findById(id);
        boolean isRole = roleById.isPresent();

        if (!isRole) {
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
        logger.info("Create Role");
        Role role = new Role();
        role.setStatus(ConstStatus.ACTIVE_STATUS);
        role.setName(request.getName());
        role.setDescription(request.getDescription());

        roleRepository.save(role);

        return RoleConverter.toDto(role);
    }
}
