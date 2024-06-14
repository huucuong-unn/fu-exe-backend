package com.exe01.backend.service.impl;

import com.exe01.backend.constant.ConstError;
import com.exe01.backend.constant.ConstStatus;
import com.exe01.backend.converter.UniversityConverter;
import com.exe01.backend.dto.UniversityDTO;
import com.exe01.backend.dto.request.university.CreateUniversityRequest;
import com.exe01.backend.dto.request.university.UpdateUniversityRequest;
import com.exe01.backend.dto.response.university.UniversityDropDownListResponse;
import com.exe01.backend.entity.University;
import com.exe01.backend.enums.ErrorCode;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.repository.UniversityRepository;
import com.exe01.backend.service.IUniversityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.exe01.backend.constant.ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_UNIVERSITY;

@Service
public class UniversityServiceImpl implements IUniversityService {

    Logger logger = LoggerFactory.getLogger(UniversityServiceImpl.class);

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public UniversityDTO create(CreateUniversityRequest request) throws BaseException {
        try {
            logger.info("Create University");
            University university = new University();
            university.setName(request.getName());
            university.setAddress(request.getAddress());
            university.setStatus(ConstStatus.ACTIVE_STATUS);

            universityRepository.save(university);

            Set<String> keysToDelete = redisTemplate.keys("University:*");
            if (keysToDelete != null && !keysToDelete.isEmpty()) {
                redisTemplate.delete(keysToDelete);
            }

            return UniversityConverter.toDTO(university);
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public Boolean update(UUID id, UpdateUniversityRequest request) throws BaseException {
        try {
            logger.info("Update university with id {}", id);
            University universityById = UniversityConverter.toEntity(findById(id));

            universityById.setId(id);
            universityById.setName(request.getName());
            universityById.setAddress(request.getAddress());

            universityRepository.save(universityById);

            Set<String> keysToDelete = redisTemplate.keys("University:*");
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
            logger.info("Delete university with id {}", id);
            var universityById = universityRepository.findById(id);
            boolean isUniversityExist = universityById.isPresent();

            if (!isUniversityExist) {
                logger.warn("University with id {} is not found", id);
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.University.UNIVERSITY_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            universityById.get().setId(id);
            universityById.get().setStatus(ConstStatus.INACTIVE_STATUS);

            universityRepository.save(universityById.get());

            Set<String> keysToDelete = redisTemplate.keys("University:*");
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
            logger.info("Delete university with id {}", id);
            University universityById = UniversityConverter.toEntity(findById(id));

            if (universityById.getStatus().equals(ConstStatus.ACTIVE_STATUS)) {
                universityById.setStatus(ConstStatus.INACTIVE_STATUS);
            } else {
                universityById.setStatus(ConstStatus.ACTIVE_STATUS);
            }

            universityRepository.save(universityById);

            Set<String> keysToDelete = redisTemplate.keys("University:*");
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
    public List<UniversityDropDownListResponse> getAll() throws BaseException {
        try {
            logger.info("Get all universities with paging");


            String cacheKey = HASH_KEY_PREFIX_FOR_UNIVERSITY + "dropDownList" + "all";

            List<UniversityDropDownListResponse> universityDropDownListResponses;

            if (redisTemplate.opsForHash().hasKey(HASH_KEY_PREFIX_FOR_UNIVERSITY, cacheKey)) {
                logger.info("Fetching universities from cache");
                universityDropDownListResponses = (List<UniversityDropDownListResponse>) redisTemplate.opsForHash().get(HASH_KEY_PREFIX_FOR_UNIVERSITY, cacheKey);
            } else {
                logger.info("Fetching universities from database");
                List<University> universities = universityRepository.findAll();
                universityDropDownListResponses = universities.stream().map(UniversityConverter::toDropDownListResponse).toList();

                redisTemplate.opsForHash().put(HASH_KEY_PREFIX_FOR_UNIVERSITY, cacheKey, universityDropDownListResponses);
            }
            return universityDropDownListResponses;
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public UniversityDTO findById(UUID id) throws BaseException {
        try {
            logger.info("Find university by id {}", id);
            String hashKey = HASH_KEY_PREFIX_FOR_UNIVERSITY + id.toString();
            UniversityDTO universityDTO = (UniversityDTO) redisTemplate.opsForHash().get(HASH_KEY_PREFIX_FOR_UNIVERSITY, hashKey);

            if (!Objects.isNull(universityDTO)) {
                return universityDTO;
            }

            Optional<University> universityById = universityRepository.findById(id);
            boolean isUniversityExist = universityById.isPresent();

            if (!isUniversityExist) {
                logger.warn("University with id {} not found", id);
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.University.UNIVERSITY_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            universityDTO = UniversityConverter.toDTO(universityById.get());

            redisTemplate.opsForHash().put(HASH_KEY_PREFIX_FOR_UNIVERSITY, hashKey, universityDTO);

            return universityDTO;
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
            logger.info("Get all universities with paging");
            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            String cacheKey = HASH_KEY_PREFIX_FOR_UNIVERSITY + "all:" + page + ":" + limit;

            List<UniversityDTO> universityDTOs;

            if (redisTemplate.opsForHash().hasKey(HASH_KEY_PREFIX_FOR_UNIVERSITY, cacheKey)) {
                logger.info("Fetching universities from cache for page {} and limit {}", page, limit);
                universityDTOs = (List<UniversityDTO>) redisTemplate.opsForHash().get(HASH_KEY_PREFIX_FOR_UNIVERSITY, cacheKey);
            } else {
                logger.info("Fetching universities from database for page {} and limit {}", page, limit);
                List<University> universities = universityRepository.findAllByOrderByCreatedDate(pageable);
                universityDTOs = universities.stream().map(UniversityConverter::toDTO).toList();

                redisTemplate.opsForHash().put(HASH_KEY_PREFIX_FOR_UNIVERSITY, cacheKey, universityDTOs);
            }

            result.setListResult(universityDTOs);
            result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
            result.setLimit(limit);

            return result;
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    private int totalItem() {
        return (int) universityRepository.count();
    }

    private int totalItemWithStatusActive() {
        return universityRepository.countByStatus(ConstStatus.ACTIVE_STATUS);
    }

    @Override
    public PagingModel findAllByStatusTrue(Integer page, Integer limit) throws BaseException {
        try {
            logger.info("Get all universities with status is active");
            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            String cacheKey = HASH_KEY_PREFIX_FOR_UNIVERSITY + "all:" + "active:" + page + ":" + limit;

            List<UniversityDTO> universityDTOs;

            if (redisTemplate.opsForHash().hasKey(HASH_KEY_PREFIX_FOR_UNIVERSITY, cacheKey)) {
                logger.info("Fetching universities from cache for page {} and limit {}", page, limit);
                universityDTOs = (List<UniversityDTO>) redisTemplate.opsForHash().get(HASH_KEY_PREFIX_FOR_UNIVERSITY, cacheKey);
            } else {
                logger.info("Fetching universities from database for page {} and limit {}", page, limit);
                List<University> universities = universityRepository.findAllByStatusOrderByCreatedDate(ConstStatus.ACTIVE_STATUS, pageable);
                universityDTOs = universities.stream().map(UniversityConverter::toDTO).toList();

                redisTemplate.opsForHash().put(HASH_KEY_PREFIX_FOR_UNIVERSITY, cacheKey, universityDTOs);
            }

            result.setListResult(universityDTOs);
            result.setTotalPage(((int) Math.ceil((double) (totalItemWithStatusActive()) / limit)));
            result.setLimit(limit);

            return result;
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }
}
