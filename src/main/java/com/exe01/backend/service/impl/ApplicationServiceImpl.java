package com.exe01.backend.service.impl;

import com.exe01.backend.constant.ConstError;
import com.exe01.backend.bucket.BucketName;
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
import com.exe01.backend.entity.Account;
import com.exe01.backend.entity.Application;
import com.exe01.backend.entity.Mentor;
import com.exe01.backend.entity.Student;
import com.exe01.backend.enums.ErrorCode;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.fileStore.FileStore;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.repository.AccountRepository;
import com.exe01.backend.repository.ApplicationRepository;
import com.exe01.backend.repository.MentorRepository;
import com.exe01.backend.service.*;
import com.exe01.backend.validation.ValidateUtil;
import org.springframework.context.annotation.Lazy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    FileStore fileStore;

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    private MentorRepository mentorRepository;

    @Override
    public ApplicationDTO create(BaseApplicationRequest request) throws BaseException {
        try {
            logger.info("Create Application");

            Application application = new Application();
            Student student = StudentConverter.toEntity(studentService.findById(request.getStudentId()));
            Account account = accountRepository.findById(student.getAccount().getId()).orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND.value(), ConstError.Account.ACCOUNT_NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase()));

            application.setStudent(student);
            application.setStatus(ConstStatus.ApplicationStatus.PROCESSING);
            application.setEmail(request.getEmail());
            application.setIntroduce(request.getIntroduce());
            application.setMentor(MentorConverter.toEntity(mentorService.findById(request.getMentorId())));
            application.setFullName(request.getFullName());
            application.setPhoneNumber(request.getPhoneNumber());
            application.setFacebookUrl(request.getFacebookUrl());
            application.setReasonApply(request.getReasonApply());
            application.setZaloAccount(request.getZaloAccount());
            application.setUserAddress(request.getUserAddress());

            int points = student.getAccount().getPoint() - 10;
            if(points>0){
account.setPoint(points);
            }
            else{
                throw  new BaseException(ErrorCode.ERROR_500.getCode(),ConstError.Account.NOT_HAVE_ENOUGH_POINT, ErrorCode.ERROR_500.getMessage());
            }

            applicationRepository.save(application);

            uploadCvFile(application.getId(), request.getCvFile());

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
            application.setStatus(ConstStatus.ACTIVE_STATUS);
            application.setEmail(request.getEmail());
            application.setIntroduce(request.getIntroduce());
            application.setMentor(MentorConverter.toEntity(mentorService.findById(request.getMentorId())));
            //application.setCvFile(request.getCvFile());
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
            Mentor mentor = mentorRepository.findById(application.getMentor().getId()).orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND.value(), ConstError.Mentor.MENTOR_NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase()));
            Account account = accountRepository.findById(mentor.getAccount().getId()).orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND.value(), ConstError.Account.ACCOUNT_NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase()));
            application.setStatus(ConstStatus.ApplicationStatus.APPROVED);

            //create mentee
            MenteeRequest menteeRequest = new MenteeRequest();
            menteeRequest.setStudentId(application.getStudent().getId());
            MenteeDTO menteeDTO = menteeService.create(menteeRequest);

            //create mentor apply
            BaseMentorApplyRequest mentorApplyRequest = new BaseMentorApplyRequest();
            mentorApplyRequest.setApplicationId(applicationId);
            mentorApplyRequest.setMenteeId(menteeDTO.getId());

            mentorApplyService.create(mentorApplyRequest);

            int points = mentor.getAccount().getPoint() - 10;
            if(points>0){
                account.setPoint(points);
            }
            else{
                throw  new BaseException(ErrorCode.ERROR_500.getCode(),ConstError.Account.NOT_HAVE_ENOUGH_POINT, ErrorCode.ERROR_500.getMessage());
            }

        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException; // rethrow the original BaseException
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());

        }

    }

    @Override
    public PagingModel findByMentorIdAndStatusAndSortByCreatedDate(UUID mentorId, String status, String createdDate, int page, int limit) throws BaseException {
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
                List<Application> applications = applicationRepository.findAllByMentorIdAndStatusAndSortByCreatedDate(mentorId, status, ConstStatus.CampaignStatus.MENTEE_APPLY, createdDate, pageable);
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
    public PagingModel findByStudentIdAndStatusAndSort(UUID studentId, UUID companyId, String mentorName, String status, String createdDate, int page, int limit) throws BaseException {

        try {
            logger.info("Get all Application");

            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            String hashKeyForApplication = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_APPLICATION + studentId + "all:" + companyId+status+ createdDate +page + ":" + limit;

            List<ApplicationDTO> applicationDTOs = new ArrayList<>();

            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_APPLICATION, hashKeyForApplication)) {
                logger.info("Fetching applications from cache for page {} and limit {}", page, limit);
                applicationDTOs = (List<ApplicationDTO>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_APPLICATION, hashKeyForApplication);
            } else {
                logger.info("Fetching applications from database for page {} and limit {}", page, limit);
                List<Application> applications = applicationRepository.findByStudentIdAndSearchSort(studentId, companyId, mentorName, status, createdDate, pageable);
                applicationDTOs = applications.stream().map(ApplicationConverter::toDto).toList();
                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_APPLICATION, hashKeyForApplication, applicationDTOs);
            }

            result.setListResult(applicationDTOs);

            result.setTotalCount(totalItem());
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
    public void uploadCvFile(UUID id, MultipartFile file) throws BaseException {
        try {
            // 1. Check if file is not empty
            if (file.isEmpty()) {
                throw new IllegalStateException("Cannot upload empty file [ " + file.getSize() + "]");
            }
            // 2. If file is a PDF
            if (!"application/pdf".equals(file.getContentType())) {
                throw new IllegalStateException("File must be a PDF [ " + file.getContentType() + "]");
            }
            // 3. The user exists in our database
            Application application = applicationRepository.findById(id).orElseThrow(() -> new IllegalStateException("Application not found"));
            // 4. Grab some metadata from file if any
            Map<String, String> metadata = new HashMap<>();
            metadata.put("Content-Type", file.getContentType());
            metadata.put("Content-Length", String.valueOf(file.getSize()));

            // 5. Store the PDF in S3 and update database (userDocumentLink) with S3 document link
            String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), id);
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            String filename = String.format("%s-%s%s", UUID.randomUUID(), originalFilename.substring(0, originalFilename.lastIndexOf('.')), extension);
            fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
            application.setCvFile(filename);

            applicationRepository.save(application);
        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw (BaseException) baseException;
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
