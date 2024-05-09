package com.exe01.backend.service.impl;

import com.exe01.backend.dto.RoleDTO;
import com.exe01.backend.dto.request.role.CreateRoleRequest;
import com.exe01.backend.dto.request.role.UpdateRoleRequest;
import com.exe01.backend.entity.Role;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.repository.RoleRepository;
import com.exe01.backend.service.IRoleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public RoleDTO findById(Long id) {
        Role role = roleRepository.findById(id);
        RoleDTO roleDTO = new RoleDTO();
        if (Objects.isNull(role)) {
            return roleDTO;
        }

        roleDTO.setName(role.getName());
        roleDTO.setDescription(role.getDescription());
        roleDTO.setStatus(role.getStatus());
        return roleDTO;
    }

    public PagingModel findAllByStatusTrue(Integer page, Integer limit) {
        PagingModel result = new PagingModel();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        List<Role> roles = roleRepository.findAllByStatusTrueOrderById(pageable);
        result.setTotalPage(((int) Math.ceil((double) (totalItemForAdmin()) / limit)));
        result.setListResult(roles.stream().map(role -> {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setId(role.getId());
            roleDTO.setName(role.getName());
            roleDTO.setDescription(role.getDescription());
            roleDTO.setStatus(role.getStatus());
            return roleDTO;
        }).collect(Collectors.toList()));
        return result;
    }

    @Override
    public List<RoleDTO> findAll() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(role -> {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setName(role.getName());
            roleDTO.setDescription(role.getDescription());
            roleDTO.setStatus(role.getStatus());
            return roleDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public Boolean checkExist(Long id) {
        Role role = roleRepository.findById(id);

        return !Objects.isNull(role);
    }

    @Override
    public PagingModel findAllWithPaging(Integer page, Integer limit) {
        PagingModel result = new PagingModel();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        List<Role> roles = roleRepository.findAllByOrderById(pageable);
        result.setTotalPage(((int) Math.ceil((double) (totalItemForAdmin()) / limit)));
        result.setListResult(roles.stream().map(role -> {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setId(role.getId());
            roleDTO.setName(role.getName());
            roleDTO.setDescription(role.getDescription());
            roleDTO.setStatus(role.getStatus());
            return roleDTO;
        }).collect(Collectors.toList()));
        return result;
    }

    @Override
    public RoleDTO create(CreateRoleRequest request) {
        Role role = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        role.setStatus(true);

        roleRepository.save(role);

        RoleDTO result = new RoleDTO();
        result.setId(role.getId());
        result.setName(role.getName());
        result.setDescription(role.getDescription());
        result.setStatus(role.getStatus());

        return result;
    }

    @Override
    public RoleDTO update(UpdateRoleRequest request, Long id) {
        Role role = roleRepository.findById(id);

        if (Objects.isNull(role)) {
            throw new EntityNotFoundException();
        }

        role.setName(request.getName());
        role.setDescription(request.getDescription());
        role.setStatus(request.getStatus());

        roleRepository.save(role);

        RoleDTO result = new RoleDTO();
        result.setId(role.getId());
        result.setName(role.getName());
        result.setDescription(role.getDescription());
        result.setStatus(role.getStatus());

        return result;
    }

    public Boolean delete(Long id) {
        Role role = roleRepository.findById(id);

        if (Objects.isNull(role)) {
            throw new EntityNotFoundException();
        }

        role.setStatus(false);

        roleRepository.save(role);

        return true;
    }

    private Integer totalItemForAdmin() {
        return (int) roleRepository.count();
    }
}
