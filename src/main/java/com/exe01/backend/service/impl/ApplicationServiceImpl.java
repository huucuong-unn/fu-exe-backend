package com.exe01.backend.service.impl;

import com.exe01.backend.constant.ConstError;
import com.exe01.backend.constant.ConstHashKeyPrefix;
import com.exe01.backend.constant.ConstStatus;
import com.exe01.backend.converter.ApplicationConverter;
import com.exe01.backend.converter.MentorConverter;
import com.exe01.backend.converter.StudentConverter;
import com.exe01.backend.dto.ApplicationDTO;
import com.exe01.backend.dto.MenteeDTO;
import com.exe01.backend.dto.request.application.BaseApplicationRequest;
import com.exe01.backend.dto.request.mentee.MenteeRequest;
import com.exe01.backend.dto.request.mentorApply.BaseMentorApplyRequest;
import com.exe01.backend.entity.Application;
import com.exe01.backend.enums.ErrorCode;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.repository.ApplicationRepository;
import com.exe01.backend.service.*;
import com.exe01.backend.validation.ValidateUtil;
import org.springframework.context.annotation.Lazy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ApplicationServiceImpl implements IApplicationService {

    Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    IMentorService mentorService;

    @Autowired
    IMenteeService menteeService;

    @Autowired
    @Lazy
    IMentorApplyService mentorApplyService;

    @Autowired
    IStudentService studentService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public ApplicationDTO create(BaseApplicationRequest request) throws BaseException {
        try {
            logger.info("Create Application");

            Application application = new Application();
            application.setStudent(StudentConverter.toEntity(studentService.findById(request.getStudentId())));
            application.setJob(request.getJob());
            application.setStatus(ConstStatus.ACTIVE_STATUS);
            application.setEmail(request.getEmail());
            application.setIntroduce(request.getIntroduce());
            application.setMentor(MentorConverter.toEntity(mentorService.findById(request.getMentorId())));
            application.setCvFile(request.getCvFile());
            application.setFullName(request.getFullName());
            application.setPhoneNumber(request.getPhoneNumber());
            application.setFacebookUrl(request.getFacebookUrl());
            application.setReasonApply(request.getReasonApply());
            application.setZaloAccount(request.getZaloAccount());
            application.setUserAddress(request.getUserAddress());

            applicationRepository.save(application);

            Set<String> keysToDelete = redisTemplate.keys("Application:*");
            if (ValidateUtil.IsNotNullOrEmptyForSet(keysToDelete)) {
                redisTemplate.delete(keysToDelete);
            }

            return ApplicationConverter.toDto(application);

        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException; // rethrow the original BaseException
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public Boolean update(UUID id, BaseApplicationRequest request) throws BaseException {
        try {
            logger.info("Create Application with request {}", request);
            logger.info("Update application");

            Application application = ApplicationConverter.toEntity(findById(id));
            application.setStudent(StudentConverter.toEntity(studentService.findById(request.getStudentId())));
            application.setJob(request.getJob());
            application.setStatus(ConstStatus.ACTIVE_STATUS);
            application.setEmail(request.getEmail());
            application.setIntroduce(request.getIntroduce());
            application.setMentor(MentorConverter.toEntity(mentorService.findById(request.getMentorId())));
            application.setCvFile(request.getCvFile());
            application.setFullName(request.getFullName());
            application.setPhoneNumber(request.getPhoneNumber());
            application.setFacebookUrl(request.getFacebookUrl());
            application.setReasonApply(request.getReasonApply());
            application.setZaloAccount(request.getZaloAccount());
            application.setUserAddress(request.getUserAddress());


            Set<String> keysToDelete = redisTemplate.keys("Application:*");
            if (ValidateUtil.IsNotNullOrEmptyForSet(keysToDelete)) {
                redisTemplate.delete(keysToDelete);
            }
            return true;

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
            logger.info("Change status application with id {}", id);
            Application application = ApplicationConverter.toEntity(findById(id));

            if (application.getStatus().equals(ConstStatus.ACTIVE_STATUS)) {
                application.setStatus(ConstStatus.INACTIVE_STATUS);
            } else {
                application.setStatus(ConstStatus.ACTIVE_STATUS);
            }

            applicationRepository.save(application);

            Set<String> keysToDelete = redisTemplate.keys("Application:*");
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
    public PagingModel findByMentorId(UUID mentorId, int page, int limit) throws BaseException {
        try {
            logger.info("Get all Application");

            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            String hashKeyForApplication = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_APPLICATION + mentorId + "all:" + page + ":" + limit;

            List<ApplicationDTO> applicationDTOs = new ArrayList<>();

            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_APPLICATION, hashKeyForApplication)) {
                logger.info("Fetching applications from cache for page {} and limit {}", page, limit);
                applicationDTOs = (List<ApplicationDTO>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_APPLICATION, hashKeyForApplication);
            } else {
                logger.info("Fetching applications from database for page {} and limit {}", page, limit);
                List<Application> applications = applicationRepository.findByMentorId(mentorId, pageable);
                applicationDTOs = applications.stream().map(ApplicationConverter::toDto).toList();
                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_APPLICATION, hashKeyForApplication, applicationDTOs);
            }

            result.setListResult(applicationDTOs);

            result.setTotalPage(((int) Math.ceil((double) (totalItemByStatusTrue()) / limit)));
            result.setLimit(limit);

            return result;

        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException; // rethrow the original BaseException
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public PagingModel findByMenteeId(UUID menteeId, int page, int limit) throws BaseException {
        try {
            logger.info("Get all Application");

            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            String hashKeyForApplication = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_APPLICATION + menteeId + "all:" + page + ":" + limit;

            List<ApplicationDTO> applicationDTOs = new ArrayList<>();

            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_APPLICATION, hashKeyForApplication)) {
                logger.info("Fetching applications from cache for page {} and limit {}", page, limit);
                applicationDTOs = (List<ApplicationDTO>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_APPLICATION, hashKeyForApplication);
            } else {
                logger.info("Fetching applications from database for page {} and limit {}", page, limit);
                List<Application> applications = applicationRepository.findByMentorId(menteeId, pageable);
                applicationDTOs = applications.stream().map(ApplicationConverter::toDto).toList();
                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_APPLICATION, hashKeyForApplication, applicationDTOs);
            }

            result.setListResult(applicationDTOs);

            result.setTotalPage(((int) Math.ceil((double) (totalItemByStatusTrue()) / limit)));
            result.setLimit(limit);

            return result;

        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException; // rethrow the original BaseException
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public void approveApplication(UUID applicationId) throws BaseException {

        try {
            Application application = ApplicationConverter.toEntity(findById(applicationId));
            application.setStatus(ConstStatus.ACTIVE_STATUS);

            //create mentee
            MenteeRequest menteeRequest = new MenteeRequest();
            menteeRequest.setStudentId(application.getStudent().getId());
            MenteeDTO menteeDTO = menteeService.create(menteeRequest);

            //create mentor apply
            BaseMentorApplyRequest mentorApplyRequest = new BaseMentorApplyRequest();
            mentorApplyRequest.setApplicationId(applicationId);
            mentorApplyRequest.setMenteeId(menteeDTO.getId());

            mentorApplyService.create(mentorApplyRequest);



        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException; // rethrow the original BaseException
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());


        }

    }

    @Override
    public ApplicationDTO findById(UUID id) throws BaseException {
        try {
            logger.info("Find Application by id {}", id);
            String hashKeyForApplication = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_APPLICATION + id.toString();
            ApplicationDTO applicationDTOByRedis = (ApplicationDTO) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_APPLICATION, hashKeyForApplication);

            if (!Objects.isNull(applicationDTOByRedis)) {
                return applicationDTOByRedis;
            }

            Optional<Application> application = applicationRepository.findById(id);
            boolean isExist = application.isPresent();

            if (!isExist) {
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Application.APPLICATION_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            ApplicationDTO applicationDTO = ApplicationConverter.toDto(application.get());

            redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_APPLICATION, hashKeyForApplication, applicationDTO);

            return applicationDTO;
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
            logger.info("Get all Application with paging");
            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            String hashKeyForApplication = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_APPLICATION + "all:" + page + ":" + limit;

            List<ApplicationDTO> applicationDTOs;

            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_APPLICATION, hashKeyForApplication)) {
                logger.info("Fetching applications from cache for page {} and limit {}", page, limit);
                applicationDTOs = (List<ApplicationDTO>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_APPLICATION, hashKeyForApplication);
            } else {
                logger.info("Fetching applications from database for page {} and limit {}", page, limit);
                List<Application> applications = applicationRepository.findAllByOrderByCreatedDate(pageable);
                applicationDTOs = applications.stream().map(ApplicationConverter::toDto).toList();
                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_APPLICATION, hashKeyForApplication, applicationDTOs);
            }

            result.setListResult(applicationDTOs);

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

    @Override
    public PagingModel findAllByStatusTrue(Integer page, Integer limit) throws BaseException {
        try {

            logger.info("Get all Application with status active");

            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            String hashKeyForApplication = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_APPLICATION + "all:" + "active:" + page + ":" + limit;

            List<ApplicationDTO> applicationDTOs = new ArrayList<>();

            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_APPLICATION, hashKeyForApplication)) {
                logger.info("Fetching applications from cache for page {} and limit {}", page, limit);
                applicationDTOs = (List<ApplicationDTO>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_APPLICATION, hashKeyForApplication);
            } else {
                logger.info("Fetching applications from database for page {} and limit {}", page, limit);
                List<Application> applications = applicationRepository.findAllByStatusOrderByCreatedDate(ConstStatus.ACTIVE_STATUS, pageable);
                applicationDTOs = applications.stream().map(ApplicationConverter::toDto).toList();
                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_APPLICATION, hashKeyForApplication, applicationDTOs);
            }

            result.setListResult(applicationDTOs);

            result.setTotalPage(((int) Math.ceil((double) (totalItemByStatusTrue()) / limit)));
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
        return (int) applicationRepository.count();
    }

    public int totalItemByStatusTrue() {
        return applicationRepository.countByStatus(ConstStatus.ACTIVE_STATUS);
    }
}
