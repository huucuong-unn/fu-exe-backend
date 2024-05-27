package com.exe01.backend.service.impl;

import com.exe01.backend.constant.ConstStatus;
import com.exe01.backend.converter.UniversityConverter;
import com.exe01.backend.dto.UniversityDTO;
import com.exe01.backend.dto.request.university.CreateUniversityRequest;
import com.exe01.backend.dto.request.university.UpdateUniversityRequest;
import com.exe01.backend.entity.University;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.repository.UniversityRepository;
import com.exe01.backend.service.IUniversityService;
import jakarta.persistence.EntityNotFoundException;
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
    public UniversityDTO create(CreateUniversityRequest request) {
        logger.info("Create University");

        University university = new University();
        university.setName(request.getName());
        university.setAddress(request.getAddress());
        university.setStatus(ConstStatus.ACTIVE_STATUS);

        universityRepository.save(university);

        return UniversityConverter.toDTO(university);
    }

    @Override
    public Boolean update(UUID id, UpdateUniversityRequest request) {
        logger.info("Update university with id {}", id);
        var universityById = universityRepository.findById(id);
        boolean isUniversityExist = universityById.isPresent();

        if (!isUniversityExist) {
            logger.warn("University with id {} is not found", id);
            throw new EntityNotFoundException();
        }

        universityById.get().setId(id);
        universityById.get().setName(request.getName());
        universityById.get().setAddress(request.getAddress());

        Set<String> keysToDelete = redisTemplate.keys("University:*");
        if (keysToDelete != null && !keysToDelete.isEmpty()) {
            redisTemplate.delete(keysToDelete);
        }

        return true;
    }

    @Override
    public Boolean delete(UUID id) {
        logger.info("Delete university with id {}", id);
        var universityById = universityRepository.findById(id);
        boolean isUniversityExist = universityById.isPresent();

        if (!isUniversityExist) {
            logger.warn("University with id {} is not found", id);
            throw new EntityNotFoundException();
        }

        universityById.get().setId(id);
        universityById.get().setStatus(ConstStatus.INACTIVE_STATUS);

        universityRepository.save(universityById.get());

        Set<String> keysToDelete = redisTemplate.keys("University:*");
        if (keysToDelete != null && !keysToDelete.isEmpty()) {
            redisTemplate.delete(keysToDelete);
        }

        return true;
    }

    @Override
    public UniversityDTO findById(UUID id) {
        logger.info("Find university by id {}", id);
        String hashKey = HASH_KEY_PREFIX_FOR_UNIVERSITY + id.toString();
        UniversityDTO universityDTO = (UniversityDTO) redisTemplate.opsForHash().get(HASH_KEY_PREFIX_FOR_UNIVERSITY, hashKey);

        if(!Objects.isNull(universityDTO)) {
            return universityDTO;
        }

        Optional<University> universityById = universityRepository.findById(id);
        boolean isUniversityExist = universityById.isPresent();

        if (!isUniversityExist) {
            logger.warn("University with id {} not found", id);
            throw new EntityNotFoundException();
        }

        universityDTO = UniversityConverter.toDTO(universityById.get());

        return universityDTO;
    }

    @Override
    public PagingModel getAll(Integer page, Integer limit) {
        logger.info("Get all universitys with paging");
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
    }

    public int totalItem() {
        return (int) universityRepository.count();
    }

    @Override
    public PagingModel findAllByStatusTrue(Integer page, Integer limit) {
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
        result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
        result.setLimit(limit);

        return result;
    }
}
