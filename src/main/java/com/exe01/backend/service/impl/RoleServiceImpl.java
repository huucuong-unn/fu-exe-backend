package com.exe01.backend.service.impl;

import com.exe01.backend.constant.ConstHashKeyPrefix;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoleServiceImpl implements IRoleService {

    Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    GenericConverter genericConverter;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public RoleDTO findById(UUID id) {
        logger.info("Find Role by id {}", id);
        String hashKeyForRole = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ROLE + id.toString();
        RoleDTO roleDTOByRedis = (RoleDTO) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ROLE, hashKeyForRole);

        if (!Objects.isNull(roleDTOByRedis)) {
            return roleDTOByRedis;
        }

        Optional<Role> roleById = roleRepository.findById(id);
        boolean isExist = roleById.isPresent();

        if (!isExist) {
            throw new EntityNotFoundException();
        }

        RoleDTO roleDTO = RoleConverter.toDto(roleById.get());

        redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ROLE, hashKeyForRole, roleDTO);

        return roleDTO;
    }

    @Override
    public PagingModel getAll(Integer page, Integer limit) {
        logger.info("Get all Role with paging");
        PagingModel result = new PagingModel();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);

        String hashKeyForRole = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ROLE + "all:" + page + ":" + limit;

        List<RoleDTO> roleDTOs;

        if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ROLE, hashKeyForRole)) {
            logger.info("Fetching roles from cache for page {} and limit {}", page, limit);
            roleDTOs = (List<RoleDTO>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ROLE, hashKeyForRole);
        } else {
            logger.info("Fetching roles from database for page {} and limit {}", page, limit);
            List<Role> roles = roleRepository.findAllByOrderByCreatedDate(pageable);
            roleDTOs = roles.stream().map(RoleConverter::toDto).toList();
            redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ROLE, hashKeyForRole, roleDTOs);
        }

        result.setListResult(roleDTOs);

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

        String hashKeyForRole = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ROLE + "all:" + "active:" + page + ":" + limit;

        List<RoleDTO> roleDTOs;

        if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ROLE, hashKeyForRole)) {
            logger.info("Fetching roles from cache for page {} and limit {}", page, limit);
            roleDTOs = (List<RoleDTO>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ROLE, hashKeyForRole);
        } else {
            logger.info("Fetching roles from database for page {} and limit {}", page, limit);
            List<Role> roles = roleRepository.findAllByStatusOrderByCreatedDate(ConstStatus.ACTIVE_STATUS, pageable);
            roleDTOs = roles.stream().map(RoleConverter::toDto).toList();
            redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ROLE, hashKeyForRole, roleDTOs);
        }

        result.setListResult(roleDTOs);

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

        Set<String> keysToDelete = redisTemplate.keys("Role:*");
        if (keysToDelete != null && !keysToDelete.isEmpty()) {
            redisTemplate.delete(keysToDelete);
        }

        return true;
    }

    @Override
    public Boolean delete(UUID id) {
        Optional<Role> roleById = roleRepository.findById(id);
        boolean isRoleExist = roleById.isPresent();

        if (!isRoleExist) {
            logger.warn("Role with id {} not found", id);
            throw new EntityNotFoundException();
        }

        roleById.get().setStatus(ConstStatus.INACTIVE_STATUS);

        roleRepository.save(roleById.get());

        Set<String> keysToDelete = redisTemplate.keys("Role:*");
        if (keysToDelete != null && !keysToDelete.isEmpty()) {
            redisTemplate.delete(keysToDelete);
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

        Set<String> keysToDelete = redisTemplate.keys("Role:*");
        if (keysToDelete != null && !keysToDelete.isEmpty()) {
            redisTemplate.delete(keysToDelete);
        }

        return RoleConverter.toDto(role);
    }
}
