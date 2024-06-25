package com.exe01.backend.service.impl;

import com.exe01.backend.constant.ConstError;
import com.exe01.backend.constant.ConstHashKeyPrefix;
import com.exe01.backend.constant.ConstStatus;
import com.exe01.backend.converter.MenteeConverter;
import com.exe01.backend.converter.StudentConverter;
import com.exe01.backend.dto.MenteeDTO;
import com.exe01.backend.dto.request.mentee.MenteeRequest;
import com.exe01.backend.entity.Student;
import com.exe01.backend.entity.Mentee;
import com.exe01.backend.enums.ErrorCode;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.repository.MenteeRepository;
import com.exe01.backend.service.IMenteeService;
import com.exe01.backend.service.IMentorService;
import com.exe01.backend.service.IStudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MenteeServiceImpl implements IMenteeService {

    Logger logger = LoggerFactory.getLogger(MenteeServiceImpl.class);


    @Autowired
    MenteeRepository menteeRepository;

    @Autowired
    IStudentService studentService;

    @Autowired
    IMentorService mentorService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public MenteeDTO findById(UUID id) throws BaseException {
        try {
            logger.info("Find mentee by id {}", id);
            String hashKeyForMentee = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTEE + id.toString();
            MenteeDTO menteeDTOByRedis = (MenteeDTO) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTEE, hashKeyForMentee);

            if (!Objects.isNull(menteeDTOByRedis)) {
                return menteeDTOByRedis;
            }

            Optional<Mentee> menteeById = menteeRepository.findById(id);
            boolean isMenteeExist = menteeById.isPresent();

            if (!isMenteeExist) {
                logger.warn("Mentee with id {} not found", id);
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Mentee.MENTEE_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            MenteeDTO menteeDTO = MenteeConverter.toDto(menteeById.get());

            redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTEE, hashKeyForMentee, menteeDTO);

            return menteeDTO;
        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException; // rethrow the original BaseException
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public PagingModel getAll(Integer page, Integer limit) throws BaseException {
        try {

            logger.info("Get all mentee");
            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            String hashKeyForMentee = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTEE + "all:" + page + ":" + limit;

            List<MenteeDTO> menteeDTOs;

            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTEE, hashKeyForMentee)) {
                logger.info("Fetching mentees from cache for page {} and limit {}", page, limit);
                menteeDTOs = (List<MenteeDTO>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTEE, hashKeyForMentee);
            } else {
                logger.info("Fetching mentees from database for page {} and limit {}", page, limit);
                List<Mentee> mentees = menteeRepository.findAllByOrderByCreatedDate(pageable);
                menteeDTOs = mentees.stream().map(MenteeConverter::toDto).toList();
                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTEE, hashKeyForMentee, menteeDTOs);
            }

            result.setListResult(menteeDTOs);
            result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
            result.setLimit(limit);

            return result;
        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException; // rethrow the original BaseException
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    public int totalItem() {
        return (int) menteeRepository.count();
    }

    public int totalItemByStatusActive() {
        return menteeRepository.countByStatus(ConstStatus.ACTIVE_STATUS);
    }

    @Override
    public PagingModel findAllByStatusTrue(Integer page, Integer limit) throws BaseException {
        try {
            logger.info("Get all mentee with status active");
            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            String hashKeyForMentee = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTEE + "all:" + "active:" + page + ":" + limit;

            List<MenteeDTO> menteeDTOs;

            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTEE, hashKeyForMentee)) {
                logger.info("Fetching mentees from cache for page {} and limit {}", page, limit);
                menteeDTOs = (List<MenteeDTO>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTEE, hashKeyForMentee);
            } else {
                logger.info("Fetching mentees from database for page {} and limit {}", page, limit);
                List<Mentee> mentees = menteeRepository.findAllByStatusOrderByCreatedDate(ConstStatus.ACTIVE_STATUS, pageable);
                menteeDTOs = mentees.stream().map(MenteeConverter::toDto).toList();
                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTEE, hashKeyForMentee, menteeDTOs);
            }

            result.setListResult(menteeDTOs);
            result.setTotalPage(((int) Math.ceil((double) (totalItemByStatusActive()) / limit)));
            result.setLimit(limit);

            return result;
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public MenteeDTO create(MenteeRequest menteeRequest) throws BaseException {
        try {

            logger.info("Create mentee");
            Student student = StudentConverter.toEntity(studentService.findById(menteeRequest.getStudentId()));

            Mentee mentee = new Mentee();
            mentee.setStudent(student);
            mentee.setStatus(ConstStatus.ACTIVE_STATUS);

            menteeRepository.save(mentee);
            return MenteeConverter.toDto(mentee);
        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException; // rethrow the original BaseException
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public Boolean changeStatus(UUID id) throws BaseException {
        try {
            logger.info("Change status mentee with id {}", id);
            Mentee mentee = MenteeConverter.toEntity(findById(id));

            if (mentee.getStatus().equals(ConstStatus.ACTIVE_STATUS)) {
                mentee.setStatus(ConstStatus.INACTIVE_STATUS);
            } else {
                mentee.setStatus(ConstStatus.ACTIVE_STATUS);
            }

            menteeRepository.save(mentee);

            Set<String> keysToDelete = redisTemplate.keys("Mentee:*");
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
    public Optional<MenteeDTO> findByStudentId(UUID studentId) throws BaseException {
        try {
            logger.info("Find mentee by student id {}", studentId);
            Optional<Mentee> menteeByStudentId = menteeRepository.findByStudentId(studentId);
            boolean isMenteeExist = menteeByStudentId.isPresent();

            if (!isMenteeExist) {
                logger.warn("Mentee with student id {} not found", studentId);
                return Optional.empty();
            }

            return Optional.of(MenteeConverter.toDto(menteeByStudentId.get()));
        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public int countAllByMentorId(UUID mentorId) throws BaseException {

        try {
            logger.info("Count all mentee by mentor id {}", mentorId);
            mentorService.findById(mentorId);
            return menteeRepository.countAllByMentorId(mentorId);
        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }

    }
}
