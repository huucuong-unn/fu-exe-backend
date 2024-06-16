package com.exe01.backend.service.impl;

import com.exe01.backend.constant.ConstError;
import com.exe01.backend.constant.ConstHashKeyPrefix;
import com.exe01.backend.constant.ConstStatus;
import com.exe01.backend.converter.CampaignConverter;
import com.exe01.backend.dto.CampaignDTO;
import com.exe01.backend.dto.request.campaign.CreateCampaignRequest;
import com.exe01.backend.dto.request.campaign.UpdateCampaignRequest;
import com.exe01.backend.entity.Campaign;
import com.exe01.backend.enums.ErrorCode;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.repository.CampaignRepository;
import com.exe01.backend.service.ICampaignService;
import com.exe01.backend.validation.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CampaignServiceImpl implements ICampaignService {
    Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    CampaignRepository campaignRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public CampaignDTO findById(UUID id) throws BaseException {
        try {
            logger.info("Find Campaign by id {}", id);
            String hashKeyForCampaign = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CAMPAIGN + id.toString();
            CampaignDTO campaignDTOByRedis = (CampaignDTO) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CAMPAIGN, hashKeyForCampaign);

            if (!Objects.isNull(campaignDTOByRedis)) {
                return campaignDTOByRedis;
            }

            Optional<Campaign> campaign = campaignRepository.findById(id);
            boolean isExist = campaign.isPresent();

            if (!isExist) {
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Campaign.CAMPAIGN_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            CampaignDTO campaignDTO = CampaignConverter.toDto(campaign.get());

            redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CAMPAIGN, hashKeyForCampaign, campaignDTO);

            return campaignDTO;
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
            logger.info("Get all Campaign with paging");
            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            String hashKeyForCampaign = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CAMPAIGN + "all:" + page + ":" + limit;

            List<CampaignDTO> campaignDTOs;

            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CAMPAIGN, hashKeyForCampaign)) {
                logger.info("Fetching campaigns from cache for page {} and limit {}", page, limit);
                campaignDTOs = (List<CampaignDTO>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CAMPAIGN, hashKeyForCampaign);
            } else {
                logger.info("Fetching campaigns from database for page {} and limit {}", page, limit);
                List<Campaign> campaigns = campaignRepository.findAllByOrderByCreatedDate(pageable);
                campaignDTOs = campaigns.stream().map(CampaignConverter::toDto).toList();
                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CAMPAIGN, hashKeyForCampaign, campaignDTOs);
            }

            result.setListResult(campaignDTOs);

            result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
            result.setTotalCount(totalItem());
            result.setLimit(limit);

            return result;
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public PagingModel findAllByStatusTrue(Integer page, Integer limit) throws BaseException {
        try {

            logger.info("Get all Campaign with status active");

            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            String hashKeyForCampaign = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CAMPAIGN + "all:" + "active:" + page + ":" + limit;

            List<CampaignDTO> campaignDTOs ;

            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CAMPAIGN, hashKeyForCampaign)) {
                logger.info("Fetching campaigns from cache for page {} and limit {}", page, limit);
                campaignDTOs = (List<CampaignDTO>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CAMPAIGN, hashKeyForCampaign);
            } else {
                logger.info("Fetching campaigns from database for page {} and limit {}", page, limit);
                List<Campaign> campaigns = campaignRepository.findAllByStatusOrderByCreatedDate(ConstStatus.ACTIVE_STATUS, pageable);
                campaignDTOs = campaigns.stream().map(CampaignConverter::toDto).toList();
                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CAMPAIGN, hashKeyForCampaign, campaignDTOs);
            }

            result.setListResult(campaignDTOs);

            result.setTotalPage(((int) Math.ceil((double) (totalItemByStatusTrue()) / limit)));
            result.setLimit(limit);

            return result;

        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    public int totalItem() {
        return (int) campaignRepository.count();
    }

    public int totalItemByStatusTrue() {
        return campaignRepository.countByStatus(ConstStatus.ACTIVE_STATUS);
    }

    @Override
    public CampaignDTO create(CreateCampaignRequest request) throws BaseException {

        try {
            logger.info("Create Role");

            Campaign campaign = new Campaign();
            campaign.setName(request.getName());
            campaign.setStartDate(request.getStartDate());
            campaign.setEndDate(request.getEndDate());
            campaign.setCompanyApplyStartDate(request.getCompanyApplyStartDate());
            campaign.setCompanyApplyEndDate(request.getCompanyApplyEndDate());
            campaign.setMenteeApplyStartDate(request.getMenteeApplyStartDate());
            campaign.setMenteeApplyEndDate(request.getMenteeApplyEndDate());
            campaign.setTrainingStartDate(request.getTrainingStartDate());
            campaign.setTrainingEndDate(request.getTrainingEndDate());
            campaign.setStatus(ConstStatus.ACTIVE_STATUS);

            campaignRepository.save(campaign);

            Set<String> keysToDelete = redisTemplate.keys("Campaign:*");
            if (ValidateUtil.IsNotNullOrEmptyForSet(keysToDelete)) {
                redisTemplate.delete(keysToDelete);
            }

            return CampaignConverter.toDto(campaign);

        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }


    @Override
    public Boolean update(UUID id, UpdateCampaignRequest request) throws BaseException {

        try {
            logger.info("Update campaign");
            Campaign campaign = CampaignConverter.toEntity(findById(id));

            campaign.setName(request.getName());
            campaign.setCompanyApplyStartDate(request.getCompanyApplyStartDate());
            campaign.setCompanyApplyEndDate(request.getCompanyApplyEndDate());
            campaign.setMenteeApplyStartDate(request.getMenteeApplyStartDate());
            campaign.setMenteeApplyEndDate(request.getMenteeApplyEndDate());
            campaign.setTrainingStartDate(request.getTrainingStartDate());
            campaign.setTrainingEndDate(request.getTrainingEndDate());

            campaignRepository.save(campaign);

            Set<String> keysToDelete = redisTemplate.keys("Campaign:*");
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
            logger.info("Change status mentee with id {}", id);
            Campaign campaign = CampaignConverter.toEntity(findById(id));

            if (campaign.getStatus().equals(ConstStatus.ACTIVE_STATUS)) {
                campaign.setStatus(ConstStatus.INACTIVE_STATUS);
            } else {
                campaign.setStatus(ConstStatus.ACTIVE_STATUS);
            }

            campaignRepository.save(campaign);

            Set<String> keysToDelete = redisTemplate.keys("Campaign:*");
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
}
