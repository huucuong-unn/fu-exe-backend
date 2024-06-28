package com.exe01.backend.service.impl;

import com.exe01.backend.constant.ConstError;
import com.exe01.backend.constant.ConstHashKeyPrefix;
import com.exe01.backend.constant.ConstStatus;
import com.exe01.backend.converter.CampaignConverter;
import com.exe01.backend.converter.CampaignMentorProfileConverter;
import com.exe01.backend.converter.MentorProfileConverter;
import com.exe01.backend.dto.CampaignMentorProfileDTO;
import com.exe01.backend.dto.request.campaignMentorProfile.CreateCampaignMentorProfileRequest;
import com.exe01.backend.dto.request.campaignMentorProfile.UpdateCampaignMentorProfileRequest;
import com.exe01.backend.entity.Account;
import com.exe01.backend.entity.CampaignMentorProfile;
import com.exe01.backend.enums.ErrorCode;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.repository.AccountRepository;
import com.exe01.backend.repository.CampaignMentorProfileRepository;
import com.exe01.backend.service.ICampaignMentorProfileService;
import com.exe01.backend.service.ICampaignService;
import com.exe01.backend.service.IMentorProfileService;
import com.exe01.backend.validation.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class CampaignMentorProfileServiceImpl implements ICampaignMentorProfileService {

    Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    CampaignMentorProfileRepository campaignMentorProfileRepository;

    @Autowired
    IMentorProfileService mentorProfileService;

    @Autowired
    ICampaignService campaignService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public CampaignMentorProfileDTO findById(UUID id) throws BaseException {
        try {
            logger.info("Find Campaign by id {}", id);
            String hashKeyForCampaignMentorProfile = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CAMPAIGN_MENTOR_PROFILE + id.toString();
            CampaignMentorProfileDTO campaignMentorProfileDTOByRedis = (CampaignMentorProfileDTO) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CAMPAIGN_MENTOR_PROFILE, hashKeyForCampaignMentorProfile);

            if (!Objects.isNull(campaignMentorProfileDTOByRedis)) {
                return campaignMentorProfileDTOByRedis;
            }

            Optional<CampaignMentorProfile> campaignMentorProfile = campaignMentorProfileRepository.findById(id);
            boolean isExist = campaignMentorProfile.isPresent();

            if (!isExist) {
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.CampaignMentorProfile.CAMPAIGN_MENTOR_PROFILE_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            CampaignMentorProfileDTO campaignMentorProfileDTO = CampaignMentorProfileConverter.toDto(campaignMentorProfile.get());

            redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CAMPAIGN_MENTOR_PROFILE, hashKeyForCampaignMentorProfile, campaignMentorProfileDTO);

            return campaignMentorProfileDTO;
        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException; // rethrow the original BaseException
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public CampaignMentorProfileDTO create(CreateCampaignMentorProfileRequest request) throws BaseException {
        try {
            logger.info("Create CampaignMentorProfile with request {}", request);

            CampaignMentorProfile campaignMentorProfile = new CampaignMentorProfile();
            campaignMentorProfile.setCampaign(CampaignConverter.toEntity(campaignService.findById(request.getCampaignId())));
            campaignMentorProfile.setMentorProfile(MentorProfileConverter.toEntity(mentorProfileService.findById(request.getMentorProfileId())));
            campaignMentorProfile.setStatus(ConstStatus.ACTIVE_STATUS);

            Account account = accountRepository.findById(campaignMentorProfile.getMentorProfile().getMentor().getCompany().getId()).orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND.value(), ConstError.Account.ACCOUNT_NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase()));

            int points = account.getPoint() - 10;
            if(points>0){
                account.setPoint(points);
            }
            else{
                throw  new BaseException(ErrorCode.ERROR_500.getCode(),ConstError.Account.NOT_HAVE_ENOUGH_POINT, ErrorCode.ERROR_500.getMessage());
            }

            Set<String> keysToDelete = redisTemplate.keys("CampaignMentorProfile:*");
            if (ValidateUtil.IsNotNullOrEmptyForSet(keysToDelete)) {
                redisTemplate.delete(keysToDelete);
            }

            return CampaignMentorProfileConverter.toDto(campaignMentorProfileRepository.save(campaignMentorProfile));

        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException; // rethrow the original BaseException
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public Boolean update(UUID id, UpdateCampaignMentorProfileRequest request) throws BaseException {
        try {
            logger.info("Create CampaignMentorProfile with request {}", request);
            logger.info("Update campaign");

            CampaignMentorProfile campaignMentorProfile = CampaignMentorProfileConverter.toEntity(findById(id));
            campaignMentorProfile.setCampaign(CampaignConverter.toEntity(campaignService.findById(request.getCampaignId())));
            campaignMentorProfile.setMentorProfile(MentorProfileConverter.toEntity(mentorProfileService.findById(request.getMentorProfileId())));

            Set<String> keysToDelete = redisTemplate.keys("CampaignMentorProfile:*");
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
            Optional<CampaignMentorProfile> campaignMentorProfileEntity = campaignMentorProfileRepository.findById(id);
            boolean isCampaignMentorProfileExist = campaignMentorProfileEntity.isPresent();

            if (!isCampaignMentorProfileExist) {
                logger.warn("CampaignMentorProfile with id {} not found", id);
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.CampaignMentorProfile.CAMPAIGN_MENTOR_PROFILE_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            if (campaignMentorProfileEntity.get().getStatus().equals(ConstStatus.ACTIVE_STATUS)) {
                campaignMentorProfileEntity.get().setStatus(ConstStatus.INACTIVE_STATUS);
            } else {
                campaignMentorProfileEntity.get().setStatus(ConstStatus.ACTIVE_STATUS);
            }

            campaignMentorProfileRepository.save(campaignMentorProfileEntity.get());

            Set<String> keysToDelete = redisTemplate.keys("CampaignMentorProfile:*");
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
    public CampaignMentorProfileDTO findByMentorIdAndStatus(UUID mentorId, String status) throws BaseException {
      try {

            logger.info("Find CampaignMentorProfile by mentorId {} and status {}", mentorId, status);
            CampaignMentorProfile campaignMentorProfile = campaignMentorProfileRepository.findByMentorProfileMentorIdAndCampaignStatus(mentorId, status);
            if (Objects.isNull(campaignMentorProfile)) {
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.CampaignMentorProfile.CAMPAIGN_MENTOR_PROFILE_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }
            return CampaignMentorProfileConverter.toDto(campaignMentorProfile);

      }catch (Exception baseException) {
          if (baseException instanceof BaseException) {
              throw baseException;
          }
          throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
      }

    }

}
