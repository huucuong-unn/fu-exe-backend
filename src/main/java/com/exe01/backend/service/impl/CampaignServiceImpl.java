package com.exe01.backend.service.impl;

import com.exe01.backend.bucket.BucketName;
import com.exe01.backend.constant.ConstError;
import com.exe01.backend.constant.ConstHashKeyPrefix;
import com.exe01.backend.constant.ConstStatus;
import com.exe01.backend.converter.CampaignConverter;
import com.exe01.backend.dto.CampaignDTO;
import com.exe01.backend.dto.request.campaign.CreateCampaignRequest;
import com.exe01.backend.dto.request.campaign.UpdateCampaignRequest;
import com.exe01.backend.entity.Account;
import com.exe01.backend.entity.Campaign;
import com.exe01.backend.enums.ErrorCode;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.fileStore.FileStore;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class CampaignServiceImpl implements ICampaignService {
    Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    CampaignRepository campaignRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    FileStore fileStore;

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
            uploadCampaignImage(campaign.getId(), request.getImg());


            Set<String> keysToDelete = redisTemplate.keys("Campaign:*");
            if (ValidateUtil.IsNotNullOrEmptyForSet(keysToDelete)) {
                redisTemplate.delete(keysToDelete);
            }

            return CampaignConverter.toDto(campaign);

        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }
    public void uploadCampaignImage(UUID campaignId, MultipartFile file) throws BaseException {
        try {
            // 1. Check if image is not empty
            if (file.isEmpty()) {
                throw new IllegalStateException("Cannot upload empty file [ " + file.getSize() + "]");
            }
            // 2. If file is an image
            if (!Arrays.asList("image/jpeg", "image/png", "image/jpg").contains(file.getContentType())) {
                throw new IllegalStateException("File must be an image [ " + file.getContentType() + "]");
            }
            // 3. The user exists in our database
            Campaign campaign = campaignRepository.findById(campaignId).orElseThrow(() -> new IllegalStateException("Campaign not found"));
            // 4. Grab some metadata from file if any
            Map<String, String> metadata = new HashMap<>();
            metadata.put("Content-Type", file.getContentType());
            metadata.put("Content-Length", String.valueOf(file.getSize()));

            // 5. Store the image in S3 and update database (userProfileImageLink) with S3 image link
            String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), campaign.getId());
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            String filename = String.format("%s-%s%s", UUID.randomUUID(), originalFilename.substring(0, originalFilename.lastIndexOf('.')), extension);
            fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
            campaign.setImg(filename);
            campaignRepository.save(campaign);
        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw (BaseException) baseException;
            }
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

    @Override
    public List<CampaignDTO> findAll() throws BaseException {
        try {

            logger.info("Get all Campaign");

            String hashKeyForCampaign = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CAMPAIGN + "all:";

            List<CampaignDTO> campaignDTOs ;

            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CAMPAIGN, hashKeyForCampaign)) {
                logger.info("Fetching campaigns from cache");
                campaignDTOs = (List<CampaignDTO>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CAMPAIGN, hashKeyForCampaign);
            } else {
                logger.info("Fetching campaigns from database");
                List<Campaign> campaigns = campaignRepository.findAll();
                campaignDTOs = campaigns.stream().map(CampaignConverter::toDto).toList();
                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CAMPAIGN, hashKeyForCampaign, campaignDTOs);
            }

            return campaignDTOs;

        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public PagingModel findAllCampaignForAdminSearch(String campaignName, String status, int page, int size) throws BaseException {

        try {
            logger.info("Get all Campaign for admin search");

            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, size);

            String hashKeyForCampaign = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CAMPAIGN + "all:" + campaignName + ":" + status + ":" + page + ":" + size;

            List<CampaignDTO> campaignDTOs ;

            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CAMPAIGN, hashKeyForCampaign)) {
                logger.info("Fetching campaigns from cache for page {} and limit {}", page, size);
                campaignDTOs = (List<CampaignDTO>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CAMPAIGN, hashKeyForCampaign);
            } else {
                logger.info("Fetching campaigns from database for page {} and limit {}", page, size);
                List<Campaign> campaigns = campaignRepository.findAllCampaignForAdminSearch(campaignName, status, pageable);
                campaignDTOs = campaigns.stream().map(CampaignConverter::toDto).toList();
                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_CAMPAIGN, hashKeyForCampaign, campaignDTOs);
            }

            result.setListResult(campaignDTOs);

            result.setTotalPage(((int) Math.ceil((double) (totalItem()) / size)));
            result.setTotalCount(totalItem());
            result.setLimit(size);

            return result;

        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }

    }
}
