package com.exe01.backend.service.impl;

import com.exe01.backend.constant.ConstError;
import com.exe01.backend.constant.ConstHashKeyPrefix;
import com.exe01.backend.constant.ConstStatus;
import com.exe01.backend.converter.ApplicationConverter;
import com.exe01.backend.converter.CampaignMentorProfileConverter;
import com.exe01.backend.converter.MentorApplyConverter;
import com.exe01.backend.converter.MenteeConverter;
import com.exe01.backend.dto.MentorApplyDTO;
import com.exe01.backend.dto.request.mentorApply.BaseMentorApplyRequest;
import com.exe01.backend.dto.response.mentorApply.MentorApplyForAdminResponse;
import com.exe01.backend.entity.MentorApply;
import com.exe01.backend.enums.ErrorCode;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.repository.MentorApplyRepository;
import com.exe01.backend.service.IApplicationService;
import com.exe01.backend.service.ICampaignMentorProfileService;
import com.exe01.backend.service.IMenteeService;
import com.exe01.backend.service.IMentorApplyService;
import com.exe01.backend.validation.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MentorApplyServiceImpl implements IMentorApplyService {
    Logger logger = LoggerFactory.getLogger(MentorApplyServiceImpl.class);

    @Autowired
    @Lazy
    IApplicationService applicationService;

    @Autowired
    IMenteeService menteeService;

    @Autowired
    MentorApplyRepository mentorApplyRepository;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    ICampaignMentorProfileService campaignMentorProfileService;

    @Override
    public MentorApplyDTO findById(UUID id) throws BaseException {
        return null;
    }

    @Override
    public MentorApplyDTO create(BaseMentorApplyRequest request) throws BaseException {
        try {
            logger.info("Create MentorApply");

            MentorApply mentorApply = new MentorApply();
            mentorApply.setFeedback(request.getFeedback());
            mentorApply.setApplication(ApplicationConverter.toEntity(applicationService.findById(request.getApplicationId())));
            mentorApply.setMentee(MenteeConverter.toEntity(menteeService.findById(request.getMenteeId())));
            mentorApply.setStatus(ConstStatus.ACTIVE_STATUS);
            mentorApply.setCampaign(CampaignMentorProfileConverter.toEntity(campaignMentorProfileService.findByMentorIdAndStatus(mentorApply.getApplication().getMentor().getId(), ConstStatus.CampaignStatus.MENTEE_APPLY)).getCampaign());

            mentorApplyRepository.save(mentorApply);

            Set<String> keysToDelete = redisTemplate.keys("MentorApply:*");
            if (ValidateUtil.IsNotNullOrEmptyForSet(keysToDelete)) {
                redisTemplate.delete(keysToDelete);
            }

            return MentorApplyConverter.toDto(mentorApply);

        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException; // rethrow the original BaseException
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public Boolean update(UUID id, BaseMentorApplyRequest request) throws BaseException {
        try {
            logger.info("Update MentorApply");

            MentorApply mentorApply = MentorApplyConverter.toEntity(MentorApplyConverter.toDto(mentorApplyRepository.findById(id).get()));

            mentorApply.setFeedback(request.getFeedback());
            mentorApply.setApplication(ApplicationConverter.toEntity(applicationService.findById(request.getApplicationId())));
            mentorApply.setMentee(MenteeConverter.toEntity(menteeService.findById(request.getMenteeId())));
            mentorApplyRepository.save(mentorApply);

            Set<String> keysToDelete = redisTemplate.keys("MentorApply:*");
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
            logger.info("Find mentorApplyService by id {}", id);
            Optional<MentorApply> mentorApplyById = mentorApplyRepository.findById(id);
            boolean isMentorApplyExist = mentorApplyById.isPresent();

            if (!isMentorApplyExist) {
                logger.warn("MentorApply with id {} is not found", id);
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.MentorApply.MENTOR_APPLY_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            if (mentorApplyById.get().getStatus().equals(ConstStatus.ACTIVE_STATUS)) {
                mentorApplyById.get().setStatus(ConstStatus.INACTIVE_STATUS);
            } else {
                mentorApplyById.get().setStatus(ConstStatus.ACTIVE_STATUS);
            }

            mentorApplyRepository.save(mentorApplyById.get());

            Set<String> keysToDelete = redisTemplate.keys("MentorApply:*");
            if (ValidateUtil.IsNotNullOrEmptyForSet(keysToDelete)) {
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
    public PagingModel findByApplicationMentorId(UUID mentorId, int page, int limit) throws BaseException {
        try {

            logger.info("Get all MentorApply with status active");

            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            String hashKeyForMentorApply = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR_APPLY + mentorId + "all:" + "active:" + page + ":" + limit;

            List<MentorApplyDTO> mentorApplyDTOs = new ArrayList<>();

            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR_APPLY, hashKeyForMentorApply)) {
                logger.info("Fetching mentorApplys from cache for page {} and limit {}", page, limit);
                mentorApplyDTOs = (List<MentorApplyDTO>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR_APPLY, hashKeyForMentorApply);
            } else {
                logger.info("Fetching mentorApplys from database for page {} and limit {}", page, limit);
                List<MentorApply> mentorApplys = mentorApplyRepository.findByApplicationMentorId(mentorId, ConstStatus.ACTIVE_STATUS, pageable);
                mentorApplyDTOs = mentorApplys.stream().map(MentorApplyConverter::toDto).toList();
                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR_APPLY, hashKeyForMentorApply, mentorApplyDTOs);
            }

            result.setListResult(mentorApplyDTOs);

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

            logger.info("Get all MentorApply with status active");

            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            String hashKeyForMentorApply = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR_APPLY + menteeId + "all:" + "active:" + page + ":" + limit;

            List<MentorApplyDTO> mentorApplyDTOs = new ArrayList<>();

            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR_APPLY, hashKeyForMentorApply)) {
                logger.info("Fetching mentorApplys from cache for page {} and limit {}", page, limit);
                mentorApplyDTOs = (List<MentorApplyDTO>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR_APPLY, hashKeyForMentorApply);
            } else {
                logger.info("Fetching mentorApplys from database for page {} and limit {}", page, limit);
                List<MentorApply> mentorApplys = mentorApplyRepository.findByMenteeIdAndStatus(menteeId, ConstStatus.ACTIVE_STATUS, pageable);
                mentorApplyDTOs = mentorApplys.stream().map(MentorApplyConverter::toDto).toList();
                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR_APPLY, hashKeyForMentorApply, mentorApplyDTOs);
            }

            result.setListResult(mentorApplyDTOs);

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
    public PagingModel findAllByMenteeNameAndMentorFullNameAndCampaignIdAndCompanyId(String menteeName, String mentorFullName, UUID campaignId, UUID companyId, int page, int limit) throws BaseException {
        try {

            logger.info("Get all MentorApply with status active");

            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            String hashKeyForMentorApply = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR_APPLY + menteeName + mentorFullName + campaignId + "all:" + page + ":" + limit;

            List<MentorApplyDTO> mentorApplyDTOs = new ArrayList<>();

            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR_APPLY, hashKeyForMentorApply)) {
                logger.info("Fetching mentorApplys from cache for page {} and limit {}", page, limit);
                mentorApplyDTOs = (List<MentorApplyDTO>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR_APPLY, hashKeyForMentorApply);
            } else {
                logger.info("Fetching mentorApplys from database for page {} and limit {}", page, limit);
                List<MentorApply> mentorApplys = mentorApplyRepository.findAllByMenteeNameAndMentorFullNameAndCampaignId(menteeName, mentorFullName, campaignId, companyId, pageable);
                mentorApplyDTOs = mentorApplys.stream().map(MentorApplyConverter::toDto).toList();
                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR_APPLY, hashKeyForMentorApply, mentorApplyDTOs);
            }

            List<MentorApplyForAdminResponse> mentorApplyForAdminResponses = mentorApplyDTOs.stream()
                    .map(mentorApplyDTO -> {
                        MentorApplyForAdminResponse response = new MentorApplyForAdminResponse();
                        response.setMentee(mentorApplyDTO.getMentee());
                        response.setMentorFullName(mentorApplyDTO.getApplication().getMentor().getFullName());
                        response.setCompanyName(mentorApplyDTO.getApplication().getMentor().getCompany().getName());
                        response.setCampaignName(mentorApplyDTO.getCampaign().getName());
                        response.setStatus(mentorApplyDTO.getStatus());
                        return response;
                    })
                    .collect(Collectors.toList());

            result.setListResult(mentorApplyForAdminResponses);

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
        return (int) mentorApplyRepository.count();
    }

    public int totalItemByStatusTrue() {
        return (int) mentorApplyRepository.countByStatus(ConstStatus.ACTIVE_STATUS);
    }

}
