package com.exe01.backend.service.impl;

import com.exe01.backend.constant.ConstError;
import com.exe01.backend.constant.ConstHashKeyPrefix;
import com.exe01.backend.constant.ConstStatus;
import com.exe01.backend.converter.GenericConverter;
import com.exe01.backend.converter.RoleConverter;
import com.exe01.backend.dto.RoleDTO;
import com.exe01.backend.dto.request.role.CreateRoleRequest;
import com.exe01.backend.dto.request.role.UpdateRoleRequest;
import com.exe01.backend.entity.Role;
import com.exe01.backend.enums.ErrorCode;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.repository.RoleRepository;
import com.exe01.backend.service.IRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public RoleDTO findById(UUID id) throws BaseException {
        try {
            logger.info("Find Role by id {}", id);
            String hashKeyForRole = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ROLE + id.toString();
            RoleDTO roleDTOByRedis = (RoleDTO) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ROLE, hashKeyForRole);

            if (!Objects.isNull(roleDTOByRedis)) {
                return roleDTOByRedis;
            }

            Optional<Role> roleById = roleRepository.findById(id);
            boolean isExist = roleById.isPresent();

            if (!isExist) {
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Role.ROLE_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            RoleDTO roleDTO = RoleConverter.toDto(roleById.get());

            redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ROLE, hashKeyForRole, roleDTO);

            return roleDTO;
        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public PagingModel getAll(Integer page, Integer limit) throws BaseException {
        try {
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
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    public int totalItem() {
        return (int) roleRepository.count();
    }

    private int totalItemWithStatusActive() {
        return (int) roleRepository.countByStatus(ConstStatus.ACTIVE_STATUS);
    }

    @Override
    public PagingModel findAllByStatusTrue(Integer page, Integer limit) throws BaseException {
        try {
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

            result.setTotalPage(((int) Math.ceil((double) (totalItemWithStatusActive()) / limit)));
            result.setLimit(limit);

            return result;
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public Boolean update(UUID id, UpdateRoleRequest request) throws BaseException {
        try {
            logger.info("Update role");
            var roleById = roleRepository.findById(id);
            boolean isRole = roleById.isPresent();

            if (!isRole) {
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Role.ROLE_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            Role role = roleById.get();
            role.setName(request.getName());
            role.setDescription(request.getDescription());
            role.setStatus(request.getStatus());

            roleRepository.save(role);

            Set<String> keysToDelete = redisTemplate.keys("Role:*");
            if (keysToDelete != null && !keysToDelete.isEmpty()) {
                redisTemplate.delete(keysToDelete);
            }

            return true;
        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public Boolean delete(UUID id) throws BaseException {
        try {
            Optional<Role> roleById = roleRepository.findById(id);
            boolean isRoleExist = roleById.isPresent();

            if (!isRoleExist) {
                logger.warn("Role with id {} not found", id);
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Role.ROLE_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            roleById.get().setStatus(ConstStatus.INACTIVE_STATUS);

            roleRepository.save(roleById.get());

            Set<String> keysToDelete = redisTemplate.keys("Role:*");
            if (keysToDelete != null && !keysToDelete.isEmpty()) {
                redisTemplate.delete(keysToDelete);
            }

            return true;
        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public Boolean changeStatus(UUID id) throws BaseException {
        try {
            Optional<Role> roleById = roleRepository.findById(id);
            boolean isRoleExist = roleById.isPresent();

            if (!isRoleExist) {
                logger.warn("Role with id {} not found", id);
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Role.ROLE_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            if (roleById.get().getStatus().equals(ConstStatus.ACTIVE_STATUS)) {
                roleById.get().setStatus(ConstStatus.INACTIVE_STATUS);
            } else {
                roleById.get().setStatus(ConstStatus.ACTIVE_STATUS);
            }

            roleRepository.save(roleById.get());

            Set<String> keysToDelete = redisTemplate.keys("Role:*");
            if (keysToDelete != null && !keysToDelete.isEmpty()) {
                redisTemplate.delete(keysToDelete);
            }

            return true;
        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public RoleDTO findByName(String name) throws BaseException {

        String hashKeyForRole = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ROLE + name;
        RoleDTO roleDTOByRedis = (RoleDTO) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ROLE, hashKeyForRole);

        if (!Objects.isNull(roleDTOByRedis)) {
            return roleDTOByRedis;
        }

        Optional<Role> roleById = roleRepository.findByName(name);
        boolean isExist = roleById.isPresent();

        if (!isExist) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Role.ROLE_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
        }

        RoleDTO roleDTO = RoleConverter.toDto(roleById.get());

        redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ROLE, hashKeyForRole, roleDTO);

        return roleDTO;
    }

    @Override
    public RoleDTO create(CreateRoleRequest request) throws BaseException {
        try {
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
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }
}
