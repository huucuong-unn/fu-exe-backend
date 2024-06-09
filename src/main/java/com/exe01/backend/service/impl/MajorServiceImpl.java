package com.exe01.backend.service.impl;

import com.exe01.backend.constant.ConstError;
import com.exe01.backend.constant.ConstStatus;
import com.exe01.backend.converter.GenericConverter;
import com.exe01.backend.converter.MajorConverter;
import com.exe01.backend.dto.MajorDTO;
import com.exe01.backend.dto.request.major.CreateMajorRequest;
import com.exe01.backend.dto.request.major.UpdateMajorRequest;
import com.exe01.backend.entity.Major;
import com.exe01.backend.enums.ErrorCode;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.repository.MajorRepository;
import com.exe01.backend.service.IMajorService;
import com.exe01.backend.validation.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.exe01.backend.constant.ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MAJOR;

@Service
public class MajorServiceImpl implements IMajorService {

    Logger logger = LoggerFactory.getLogger(MajorServiceImpl.class);

    @Autowired
    GenericConverter genericConverter;

    @Autowired
    private MajorRepository majorRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public MajorDTO findById(UUID id) throws BaseException {
        try {
            logger.info("Find major by id {}", id);
            String hashKey = HASH_KEY_PREFIX_FOR_MAJOR + id.toString();
            MajorDTO majorDTO = (MajorDTO) redisTemplate.opsForHash().get(HASH_KEY_PREFIX_FOR_MAJOR, hashKey);

            if (!Objects.isNull(majorDTO)) {
                return majorDTO;
            }

            Optional<Major> majorById = majorRepository.findById(id);
            boolean isMajorExist = majorById.isPresent();

            if (!isMajorExist) {
                logger.warn("Major with id {} not found", id);
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Major.Major_NOT_FOUND, ErrorCode.ERROR_500.getMessage());

            }

            majorDTO = MajorConverter.toDto(majorById.get());

            redisTemplate.opsForHash().put(HASH_KEY_PREFIX_FOR_MAJOR, hashKey, majorDTO);

            return majorDTO;
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
            logger.info("Get all major");
            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            String cacheKey = HASH_KEY_PREFIX_FOR_MAJOR + "all:" + page + ":" + limit;

            List<MajorDTO> majorDTOs;

            if (redisTemplate.opsForHash().hasKey(HASH_KEY_PREFIX_FOR_MAJOR, cacheKey)) {
                logger.info("Fetching majors from cache for page {} and limit {}", page, limit);
                majorDTOs = (List<MajorDTO>) redisTemplate.opsForHash().get(HASH_KEY_PREFIX_FOR_MAJOR, cacheKey);
            } else {
                logger.info("Fetching majors from database for page {} and limit {}", page, limit);
                List<Major> majors = majorRepository.findAllByOrderByCreatedDate(pageable);
                majorDTOs = majors.stream().map(MajorConverter::toDto).toList();

                redisTemplate.opsForHash().put(HASH_KEY_PREFIX_FOR_MAJOR, cacheKey, majorDTOs);
            }

            result.setListResult(majorDTOs);
            result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
            result.setLimit(limit);

            return result;
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    public int totalItem() {
        return (int) majorRepository.count();
    }

    private int totalItemWithStatusActive() {
        return majorRepository.countByStatus(ConstStatus.ACTIVE_STATUS);
    }

    @Override
    public PagingModel findAllByStatusTrue(Integer page, Integer limit) throws BaseException {
        try {
            logger.info("Get all major with status true");
            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            String cacheKey = HASH_KEY_PREFIX_FOR_MAJOR + "all:" + "active:" + page + ":" + limit;

            List<MajorDTO> majorDTOs;

            if (redisTemplate.opsForHash().hasKey(HASH_KEY_PREFIX_FOR_MAJOR, cacheKey)) {
                logger.info("Fetching majors from cache for page {} and limit {}", page, limit);
                majorDTOs = (List<MajorDTO>) redisTemplate.opsForHash().get(HASH_KEY_PREFIX_FOR_MAJOR, cacheKey);
            } else {
                logger.info("Fetching majors from database for page {} and limit {}", page, limit);
                List<Major> majors = majorRepository.findAllByStatusOrderByCreatedDate(ConstStatus.ACTIVE_STATUS, pageable);
                majorDTOs = majors.stream().map(MajorConverter::toDto).toList();

                redisTemplate.opsForHash().put(HASH_KEY_PREFIX_FOR_MAJOR, cacheKey, majorDTOs);
            }

            result.setListResult(majorDTOs);
            result.setTotalPage(((int) Math.ceil((double) (totalItemWithStatusActive()) / limit)));
            result.setLimit(limit);

            return result;
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public MajorDTO create(CreateMajorRequest request) throws BaseException {
        try {
            logger.info("Create major");
            Major major = new Major();
            major.setStatus(ConstStatus.ACTIVE_STATUS);
            major.setName(request.getName());
            major.setDescription(request.getDescription());

            majorRepository.save(major);

            Set<String> keysToDelete = redisTemplate.keys("Major:*");
            if (ValidateUtil.IsNotNullOrEmptyForSet(keysToDelete)) {
                redisTemplate.delete(keysToDelete);
            }

            return MajorConverter.toDto(major);
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public Boolean update(UUID id, UpdateMajorRequest request) throws BaseException {
        try {
            logger.info("Update major with id {}", id);
            logger.info("Find major by id {}", id);
            Major majorById = MajorConverter.toEntity(findById(id));

            majorById.setId(id);
            majorById.setName(request.getName());
            majorById.setDescription(request.getDescription());
            majorById.setStatus(request.getStatus());

            majorRepository.save(majorById);

            Set<String> keysToDelete = redisTemplate.keys("Major:*");
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
            logger.info("Delete major with id {}", id);
            Major majorById = MajorConverter.toEntity(findById(id));

            majorById.setId(id);
            majorById.setStatus(ConstStatus.INACTIVE_STATUS);

            majorRepository.save(majorById);

            Set<String> keysToDelete = redisTemplate.keys("Major:*");
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
            logger.info("Change status major with id {}", id);
            Major majorById = MajorConverter.toEntity(findById(id));

            if (majorById.getStatus().equals(ConstStatus.ACTIVE_STATUS)) {
                majorById.setStatus(ConstStatus.INACTIVE_STATUS);
            } else {
                majorById.setStatus(ConstStatus.ACTIVE_STATUS);
            }

            majorRepository.save(majorById);

            Set<String> keysToDelete = redisTemplate.keys("Major:*");
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
}
