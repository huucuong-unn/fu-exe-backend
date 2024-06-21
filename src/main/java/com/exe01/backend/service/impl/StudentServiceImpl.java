package com.exe01.backend.service.impl;

import com.exe01.backend.constant.ConstError;
import com.exe01.backend.constant.ConstStatus;
import com.exe01.backend.converter.AccountConverter;
import com.exe01.backend.converter.StudentConverter;
import com.exe01.backend.converter.UniversityConverter;
import com.exe01.backend.dto.AccountDTO;
import com.exe01.backend.dto.MenteeDTO;
import com.exe01.backend.dto.StudentDTO;
import com.exe01.backend.dto.request.student.CreateStudentRequest;
import com.exe01.backend.dto.request.student.UpdateStudentRequest;
import com.exe01.backend.entity.Account;
import com.exe01.backend.entity.Student;
import com.exe01.backend.entity.University;
import com.exe01.backend.enums.ErrorCode;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.repository.StudentRepository;
import com.exe01.backend.service.IAccountService;
import com.exe01.backend.service.IStudentService;
import com.exe01.backend.service.IUniversityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.exe01.backend.constant.ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_STUDENT;

@Service
public class StudentServiceImpl implements IStudentService {

    Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    @Lazy
    private IAccountService accountService;

    @Autowired
    private IUniversityService universityService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public StudentDTO create(CreateStudentRequest request) throws BaseException {
        try {
            logger.info("Create student");

            Student student = new Student();
            student.setStudentCode(request.getStudentCode());
            student.setName(request.getName());
            student.setDob(request.getDob());
            student.setStatus(ConstStatus.ACTIVE_STATUS);

            Account accountById = AccountConverter.toEntity(accountService.findById(request.getAccountId()));

            if (accountById.getStatus().equals(ConstStatus.INACTIVE_STATUS)) {
                logger.warn("Account with id {} is not found", request.getUniversityId());
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Account.ACCOUNT_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            student.setAccount(accountById);

            University universityById = UniversityConverter.toEntity(universityService.findById(request.getUniversityId()));

            if (universityById.getStatus().equals(ConstStatus.INACTIVE_STATUS)) {
                logger.warn("University with id {} is not found", request.getUniversityId());
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.University.UNIVERSITY_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            student.setUniversity(universityById);

            studentRepository.save(student);

            Set<String> keysToDelete = redisTemplate.keys("Student:*");
            if (keysToDelete != null && !keysToDelete.isEmpty()) {
                redisTemplate.delete(keysToDelete);
            }

            return StudentConverter.toDto(student);
        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }

    }

    @Override
    public Boolean update(UUID id, UpdateStudentRequest request) throws BaseException {
        try {
            logger.info("Update student with id {}", id);
            Student studentById = StudentConverter.toEntity(findById(id));

            Account accountById = AccountConverter.toEntity(accountService.findById(request.getAccountId()));

            if (accountById.getStatus().equals(ConstStatus.INACTIVE_STATUS)) {
                logger.warn("Account with id {} is not found", request.getUniversityId());
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Account.ACCOUNT_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            University universityById = UniversityConverter.toEntity(universityService.findById(request.getUniversityId()));

            if (universityById.getStatus().equals(ConstStatus.INACTIVE_STATUS)) {
                logger.warn("University with id {} is not found", request.getUniversityId());
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.University.UNIVERSITY_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            studentById.setId(id);
            studentById.setName(request.getName());
            studentById.setStudentCode(request.getStudentCode());
            studentById.setDob(request.getDob());
            studentById.setAccount(accountById);
            studentById.setUniversity(universityById);

            studentRepository.save(studentById);

            Set<String> keysToDelete = redisTemplate.keys("Student:*");
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
            logger.info("Delete student with id {}", id);
            var studentById = studentRepository.findById(id);
            boolean isStudentExist = studentById.isPresent();

            if (!isStudentExist) {
                logger.warn("Student with id {} is not found", id);
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Student.STUDENT_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            studentById.get().setId(id);
            studentById.get().setStatus(ConstStatus.INACTIVE_STATUS);

            studentRepository.save(studentById.get());

            Set<String> keysToDelete = redisTemplate.keys("Student:*");
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
            logger.info("Delete student with id {}", id);
            Student studentById = StudentConverter.toEntity(findById(id));

            if (studentById.getStatus().equals(ConstStatus.ACTIVE_STATUS)) {
                studentById.setStatus(ConstStatus.INACTIVE_STATUS);
            } else {
                studentById.setStatus(ConstStatus.ACTIVE_STATUS);
            }

            studentRepository.save(studentById);

            Set<String> keysToDelete = redisTemplate.keys("Student:*");
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
    public StudentDTO findByAccountId(UUID accountId) throws BaseException {
        try {
            logger.info("Find student by account id {}", accountId);
            Optional<Student> studentByAccountId = studentRepository.findByAccountId(accountId);
            boolean isStudentExist = studentByAccountId.isPresent();

            if (!isStudentExist) {
                logger.warn("Student with account id {} not found", accountId);
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Student.STUDENT_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            return StudentConverter.toDto(studentByAccountId.get());
        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public StudentDTO findById(UUID id) throws BaseException {
        try {
            logger.info("Find student by id {}", id);
            String hashKey = HASH_KEY_PREFIX_FOR_STUDENT + id.toString();
            StudentDTO studentDTO = (StudentDTO) redisTemplate.opsForHash().get(HASH_KEY_PREFIX_FOR_STUDENT, hashKey);

            if (!Objects.isNull(studentDTO)) {
                return studentDTO;
            }

            Optional<Student> studentById = studentRepository.findById(id);
            boolean isStudentExist = studentById.isPresent();

            if (!isStudentExist) {
                logger.warn("Student with id {} not found", id);
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Student.STUDENT_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            studentDTO = StudentConverter.toDto(studentById.get());

            redisTemplate.opsForHash().put(HASH_KEY_PREFIX_FOR_STUDENT, hashKey, studentDTO);

            return studentDTO;
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
            logger.info("Get all students with paging");
            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            String cacheKey = HASH_KEY_PREFIX_FOR_STUDENT + "all:" + page + ":" + limit;

            List<StudentDTO> studentDTOs;

            if (redisTemplate.opsForHash().hasKey(HASH_KEY_PREFIX_FOR_STUDENT, cacheKey)) {
                logger.info("Fetching students from cache for page {} and limit {}", page, limit);
                studentDTOs = (List<StudentDTO>) redisTemplate.opsForHash().get(HASH_KEY_PREFIX_FOR_STUDENT, cacheKey);
            } else {
                logger.info("Fetching students from database for page {} and limit {}", page, limit);
                List<Student> students = studentRepository.findAllByOrderByCreatedDate(pageable);
                studentDTOs = students.stream().map(StudentConverter::toDto).toList();

                redisTemplate.opsForHash().put(HASH_KEY_PREFIX_FOR_STUDENT, cacheKey, studentDTOs);
            }

            result.setListResult(studentDTOs);
            result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
            result.setLimit(limit);

            return result;
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    public int totalItem() {
        return (int) studentRepository.count();
    }

    private int totalItemWithStatusActive() {
        return studentRepository.countByStatus(ConstStatus.ACTIVE_STATUS);
    }

    @Override
    public PagingModel findAllByStatusTrue(Integer page, Integer limit) throws BaseException {
        try {
            logger.info("Get all students with status is active");
            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            String cacheKey = HASH_KEY_PREFIX_FOR_STUDENT + "all:" + "active:" + page + ":" + limit;

            List<StudentDTO> studentDTOs;

            if (redisTemplate.opsForHash().hasKey(HASH_KEY_PREFIX_FOR_STUDENT, cacheKey)) {
                logger.info("Fetching students from cache for page {} and limit {}", page, limit);
                studentDTOs = (List<StudentDTO>) redisTemplate.opsForHash().get(HASH_KEY_PREFIX_FOR_STUDENT, cacheKey);
            } else {
                logger.info("Fetching students from database for page {} and limit {}", page, limit);
                List<Student> students = studentRepository.findAllByStatusOrderByCreatedDate(ConstStatus.ACTIVE_STATUS, pageable);
                studentDTOs = students.stream().map(StudentConverter::toDto).toList();

                redisTemplate.opsForHash().put(HASH_KEY_PREFIX_FOR_STUDENT, cacheKey, studentDTOs);
            }

            result.setListResult(studentDTOs);
            result.setTotalPage(((int) Math.ceil((double) (totalItemWithStatusActive()) / limit)));
            result.setLimit(limit);

            return result;
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

}
