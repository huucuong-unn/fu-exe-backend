package com.exe01.backend.service.impl;

import com.exe01.backend.converter.RoleConverter;
import com.exe01.backend.dto.MentorDTO;
import com.exe01.backend.dto.RoleDTO;
import com.exe01.backend.dto.request.role.CreateRoleRequest;
import com.exe01.backend.dto.request.role.UpdateRoleRequest;
import com.exe01.backend.entity.Role;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.repository.RoleRepository;
import com.exe01.backend.service.IMentorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public class MentorServiceImple implements IMentorService {

    @Override
    public MentorDTO findById(UUID id) {
        return null;
    }

    @Override
    public PagingModel getAll(Integer page, Integer limit) {
        return null;
    }

    @Override
    public PagingModel findAllByStatusTrue(Integer page, Integer limit) {
        return null;
    }
}
