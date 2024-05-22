package com.exe01.backend.service.impl;

import com.exe01.backend.constant.ConstStatus;
import com.exe01.backend.converter.GenericConverter;
import com.exe01.backend.converter.MajorConverter;
import com.exe01.backend.dto.MajorDTO;
import com.exe01.backend.dto.request.major.CreateMajorRequest;
import com.exe01.backend.dto.request.major.UpdateMajorRequest;
import com.exe01.backend.entity.Major;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.repository.MajorRepository;
import com.exe01.backend.service.IMajorService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MajorServiceImpl implements IMajorService {

    Logger logger = LoggerFactory.getLogger(MajorServiceImpl.class);

    @Autowired
    GenericConverter genericConverter;

    @Autowired
    private MajorRepository majorRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String HASH_KEY_PREFIX = "Major:";

    @Override
    public MajorDTO findById(UUID id) {
        logger.info("Find major by id {}", id);
        String hashKey = HASH_KEY_PREFIX + id.toString();
        MajorDTO majorDTO = (MajorDTO) redisTemplate.opsForHash().get(HASH_KEY_PREFIX, hashKey);

        if (!Objects.isNull(majorDTO)) {
            return majorDTO;
        }

        Optional<Major> majorById = majorRepository.findById(id);
        boolean isMajorExist = majorById.isPresent();

        if (!isMajorExist) {
            logger.warn("Major with id {} not found", id);
            throw new EntityNotFoundException();
        }

        majorDTO = MajorConverter.toDTO(majorById.get());

        redisTemplate.opsForHash().put(HASH_KEY_PREFIX, hashKey, majorDTO);

        return majorDTO;
    }

    @Override
    public PagingModel getAll(Integer page, Integer limit) {
        logger.info("Get all major");
        PagingModel result = new PagingModel();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);

        String cacheKey = HASH_KEY_PREFIX + "all:" + page + ":" + limit;

        List<MajorDTO> majorDTOs;

        if (redisTemplate.opsForHash().hasKey(HASH_KEY_PREFIX, cacheKey)) {
            logger.info("Fetching majors from cache for page {} and limit {}", page, limit);
            majorDTOs = (List<MajorDTO>) redisTemplate.opsForHash().get(HASH_KEY_PREFIX, cacheKey);
        } else {
            logger.info("Fetching majors from database for page {} and limit {}", page, limit);
            List<Major> majors = majorRepository.findAllByOrderByCreatedDate(pageable);
            majorDTOs = majors.stream().map(MajorConverter::toDTO).toList();

            redisTemplate.opsForHash().put(HASH_KEY_PREFIX, cacheKey, majorDTOs);
        }

        result.setListResult(majorDTOs);
        result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
        result.setLimit(limit);

        return result;
    }

    public int totalItem() {
        return (int) majorRepository.count();
    }

    @Override
    public PagingModel findAllByStatusTrue(Integer page, Integer limit) {
        logger.info("Get all major with status true");
        PagingModel result = new PagingModel();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);

        String cacheKey = HASH_KEY_PREFIX + "all:" + "active:" + page + ":" + limit;

        List<MajorDTO> majorDTOs;

        if (redisTemplate.opsForHash().hasKey(HASH_KEY_PREFIX, cacheKey)) {
            logger.info("Fetching majors from cache for page {} and limit {}", page, limit);
            majorDTOs = (List<MajorDTO>) redisTemplate.opsForHash().get(HASH_KEY_PREFIX, cacheKey);
        } else {
            logger.info("Fetching majors from database for page {} and limit {}", page, limit);
            List<Major> majors = majorRepository.findAllByStatusOrderByCreatedDate(ConstStatus.ACTIVE_STATUS, pageable);
            majorDTOs = majors.stream().map(MajorConverter::toDTO).toList();

            redisTemplate.opsForHash().put(HASH_KEY_PREFIX, cacheKey, majorDTOs);
        }

        result.setListResult(majorDTOs);
        result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
        result.setLimit(limit);

        return result;
    }

    @Override
    public MajorDTO create(CreateMajorRequest request) {
        logger.info("Create major");
        Major major = new Major();
        major.setStatus(ConstStatus.ACTIVE_STATUS);
        major.setName(request.getName());
        major.setDescription(request.getDescription());

        majorRepository.save(major);

        return MajorConverter.toDTO(major);
    }

    @Override
    public Boolean update(UUID id, UpdateMajorRequest request) {
        logger.info("Update major with id {}", id);
        logger.info("Find major by id {}", id);
        var majorById = majorRepository.findById(id);
        boolean isMajorExist = majorById.isPresent();

        if (!isMajorExist) {
            logger.warn("Major with id {} not found", id);
            throw new EntityNotFoundException();
        }

        majorById.get().setId(id);
        majorById.get().setName(request.getName());
        majorById.get().setDescription(request.getDescription());
        majorById.get().setStatus(request.getStatus());

        majorRepository.save(majorById.get());

        Set<String> keysToDelete = redisTemplate.keys("Major:*");
        if (keysToDelete != null && !keysToDelete.isEmpty()) {
            redisTemplate.delete(keysToDelete);
        }

        return true;
    }

    @Override
    public Boolean delete(UUID id) {
        logger.info("Delete major with id {}", id);
        var majorById = majorRepository.findById(id);
        boolean isMajorExist = majorById.isPresent();

        if (!isMajorExist) {
            logger.warn("Major with id {} not found", id);
            throw new EntityNotFoundException();
        }

        majorById.get().setId(id);
        majorById.get().setStatus(ConstStatus.INACTIVE_STATUS);

        majorRepository.save(majorById.get());

        Set<String> keysToDelete = redisTemplate.keys("Major:*");
        if (keysToDelete != null && !keysToDelete.isEmpty()) {
            redisTemplate.delete(keysToDelete);
        }

        return true;
    }
}
