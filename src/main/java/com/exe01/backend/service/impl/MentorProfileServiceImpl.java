package com.exe01.backend.service.impl;

import com.exe01.backend.constant.ConstError;
import com.exe01.backend.constant.ConstHashKeyPrefix;
import com.exe01.backend.constant.ConstStatus;
import com.exe01.backend.converter.GenericConverter;
import com.exe01.backend.converter.MentorConverter;
import com.exe01.backend.converter.MentorProfileConverter;
import com.exe01.backend.dto.MentorProfileDTO;
import com.exe01.backend.dto.request.mentorProfile.CreateMentorProfileRequest;
import com.exe01.backend.dto.request.mentorProfile.UpdateMentorProfileRequest;
import com.exe01.backend.dto.response.mentorProfile.MentorsResponse;
import com.exe01.backend.entity.Account;
import com.exe01.backend.entity.Mentor;
import com.exe01.backend.entity.MentorProfile;
import com.exe01.backend.enums.ErrorCode;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.repository.AccountRepository;
import com.exe01.backend.repository.MentorProfileRepository;
import com.exe01.backend.repository.MentorRepository;
import com.exe01.backend.service.IMentorProfileService;
import com.exe01.backend.service.IMentorService;
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
public class MentorProfileServiceImpl implements IMentorProfileService {

    Logger logger = LoggerFactory.getLogger(MentorProfileServiceImpl.class);

    @Autowired
    GenericConverter genericConverter;

    @Autowired
    MentorProfileRepository mentorProfileRepository;

    @Autowired
    MentorRepository mentorRepository;

    @Autowired
    IMentorService mentorService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public MentorProfileDTO findById(UUID id) throws BaseException {
        try {

            logger.info("Find mentor profile by id {}", id);
            String hashKeyForMentorProfile = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR_PROFILE + id.toString();
            MentorProfileDTO mentorProfileDTOByRedis = (MentorProfileDTO) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR_PROFILE, hashKeyForMentorProfile);

            if (!Objects.isNull(mentorProfileDTOByRedis)) {
                return mentorProfileDTOByRedis;
            }

            Optional<MentorProfile> mentorProfileById = mentorProfileRepository.findById(id);
            boolean isMentorExist = mentorProfileById.isPresent();

            if (!isMentorExist) {
                logger.warn("Mentor profile with id {} not found", id);
                throw new EntityNotFoundException();
            }

            MentorProfileDTO mentorProfileDTO = MentorProfileConverter.toDto(mentorProfileById.get());

            redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR_PROFILE, hashKeyForMentorProfile, mentorProfileDTO);

            return mentorProfileDTO;
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public PagingModel getAll(Integer page, Integer limit) throws BaseException {

        try {

            logger.info("Get all mentor profile");
            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            String hashKeyForMentorProfile = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR_PROFILE + "all:" + page + ":" + limit;

            List<MentorProfileDTO> mentorProfileDTOs;

            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR_PROFILE, hashKeyForMentorProfile)) {
                logger.info("Fetching mentor profile from cache for page {} and limit {}", page, limit);
                mentorProfileDTOs = (List<MentorProfileDTO>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR_PROFILE, hashKeyForMentorProfile);
            } else {
                logger.info("Fetching mentor profile from database for page {} and limit {}", page, limit);
                List<MentorProfile> mentorProfiles = mentorProfileRepository.findAllByOrderByCreatedDate(pageable);
                mentorProfileDTOs = mentorProfiles.stream().map(MentorProfileConverter::toDto).toList();
                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR_PROFILE, hashKeyForMentorProfile, mentorProfileDTOs);
            }

            result.setListResult(mentorProfileDTOs);
            result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
            result.setLimit(limit);

            return result;
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    public int totalItem() {
        return (int) mentorProfileRepository.count();
    }

    @Override
    public PagingModel findAllByStatusTrue(Integer page, Integer limit) throws BaseException {

        try {

            logger.info("Get all mentor profile with status is ACTIVE");
            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            String hashKeyForMentorProfile = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR_PROFILE + "all:" + "active:" + page + ":" + limit;

            List<MentorProfileDTO> mentorProfileDTOs;

            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR_PROFILE, hashKeyForMentorProfile)) {
                logger.info("Fetching mentor profile from cache for page {} and limit {}", page, limit);
                mentorProfileDTOs = (List<MentorProfileDTO>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR_PROFILE, hashKeyForMentorProfile);
            } else {
                logger.info("Fetching mentor profile from database for page {} and limit {}", page, limit);
                List<MentorProfile> mentorProfiles = mentorProfileRepository.findAllByStatusOrderByCreatedDate(ConstStatus.ACTIVE_STATUS, pageable);
                mentorProfileDTOs = mentorProfiles.stream().map(MentorProfileConverter::toDto).toList();
                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR_PROFILE, hashKeyForMentorProfile, mentorProfileDTOs);
            }

            result.setListResult(mentorProfileDTOs);
            result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
            result.setLimit(limit);

            return result;
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public MentorProfileDTO create(CreateMentorProfileRequest request) throws BaseException {

        try {

            logger.info("Create mentor profile");
            MentorProfile mentorProfile = new MentorProfile();

            Mentor mentorById = MentorConverter.toEntity(mentorService.findById(request.getMentorId()));
            Account account = accountRepository.findById(mentorById.getAccount().getId()).orElseThrow(() -> new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Account.ACCOUNT_NOT_FOUND, ErrorCode.ERROR_500.getMessage()));

            mentorProfile.setMentor(mentorById);
            mentorProfile.setLinkedinUrl(request.getLinkedinUrl());
            mentorProfile.setRequirement(request.getRequirement());
            mentorProfile.setDescription(request.getDescription());
            mentorProfile.setShortDescription(request.getShortDescription());
            mentorProfile.setProfilePicture(request.getProfilePicture());
            mentorProfile.setStatus(ConstStatus.ACTIVE_STATUS);

            mentorProfileRepository.save(mentorProfile);

            int points = account.getPoint() - 10;
            if(points>0){
                account.setPoint(points);
                accountRepository.save(account);
            }
            else{
                throw  new BaseException(ErrorCode.ERROR_500.getCode(),ConstError.Account.NOT_HAVE_ENOUGH_POINT, ErrorCode.ERROR_500.getMessage());
            }

            return MentorProfileConverter.toDto(mentorProfile);

        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException; // rethrow the original BaseException
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public Boolean update(UUID id, UpdateMentorProfileRequest request) throws BaseException {

        try {

            logger.info("Update mentor profile with id {}", id);
            logger.info("Find mentor profile by id {}", id);
            MentorProfile mentorProfileById = MentorProfileConverter.toEntity(findById(id));

            Mentor mentorById = MentorConverter.toEntity(mentorService.findById(request.getMentorId()));

            mentorProfileById.setId(id);
            mentorProfileById.setMentor(mentorById);
            mentorProfileById.setLinkedinUrl(request.getLinkedinUrl());
            mentorProfileById.setRequirement(request.getRequirement());
            mentorProfileById.setDescription(request.getDescription());
            mentorProfileById.setShortDescription(request.getShortDescription());
            mentorProfileById.setProfilePicture(request.getProfilePicture());
            mentorProfileById.setStatus(request.getStatus());

            mentorProfileRepository.save(mentorProfileById);

            Set<String> keysToDelete = redisTemplate.keys("MentorProfile:*");
            if (keysToDelete != null && !keysToDelete.isEmpty()) {
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
    public Boolean delete(UUID id) throws BaseException {
        try {

            logger.info("Delete mentor profile with id {}", id);
            var mentorById = mentorProfileRepository.findById(id);
            boolean isMentorExist = mentorById.isPresent();

            if (!isMentorExist) {
                logger.warn("Mentor profile with id {} not found", id);
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.MentorProfile.MENTOR_PROFILE_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            mentorById.get().setId(id);
            mentorById.get().setStatus(ConstStatus.INACTIVE_STATUS);

            mentorProfileRepository.save(mentorById.get());

            Set<String> keysToDelete = redisTemplate.keys("MentorProfile:*");
            if (keysToDelete != null && !keysToDelete.isEmpty()) {
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
            logger.info("Change status mentor profile with id {}", id);
            MentorProfile mentorProfileById = MentorProfileConverter.toEntity(findById(id));

            mentorProfileById.setId(id);

            if (mentorProfileById.getStatus().equals(ConstStatus.ACTIVE_STATUS)) {
                mentorProfileById.setStatus(ConstStatus.INACTIVE_STATUS);
            } else {
                mentorProfileById.setStatus(ConstStatus.ACTIVE_STATUS);
            }

            mentorProfileRepository.save(mentorProfileById);

            Set<String> keysToDelete = redisTemplate.keys("MentorProfile:*");
            if (keysToDelete != null && !keysToDelete.isEmpty()) {
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

}
