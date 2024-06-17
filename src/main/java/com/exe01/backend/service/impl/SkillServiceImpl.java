package com.exe01.backend.service.impl;

import com.exe01.backend.constant.ConstError;
import com.exe01.backend.constant.ConstHashKeyPrefix;
import com.exe01.backend.constant.ConstStatus;
import com.exe01.backend.converter.CompanyConverter;
import com.exe01.backend.converter.GenericConverter;
import com.exe01.backend.converter.MajorConverter;
import com.exe01.backend.converter.SkillConverter;
import com.exe01.backend.dto.SkillDTO;
import com.exe01.backend.dto.request.skill.CreateSkillRequest;
import com.exe01.backend.dto.request.skill.UpdateSkillRequest;
import com.exe01.backend.dto.response.skill.AllSkillOfCompanyResponse;
import com.exe01.backend.entity.Company;
import com.exe01.backend.entity.Major;
import com.exe01.backend.entity.Skill;
import com.exe01.backend.enums.ErrorCode;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.repository.SkillRepository;
import com.exe01.backend.service.ICompanyService;
import com.exe01.backend.service.IMajorService;
import com.exe01.backend.service.ISkillService;
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
public class SkillServiceImpl implements ISkillService {

    Logger logger = LoggerFactory.getLogger(SkillServiceImpl.class);

    @Autowired
    GenericConverter genericConverter;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private IMajorService majorService;

    @Autowired
    private ICompanyService companyService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public SkillDTO findById(UUID id) throws BaseException {
        try {
            logger.info("Find Skill by id {}", id);
            String hashKeyForSkill = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL + id.toString();
            SkillDTO skillDTOByRedis = (SkillDTO) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL, hashKeyForSkill);

            if (!Objects.isNull(skillDTOByRedis)) {
                return skillDTOByRedis;
            }

            Optional<Skill> skillById = skillRepository.findById(id);
            boolean isSkillExist = skillById.isPresent();

            if (!isSkillExist) {
                logger.warn("Skill with id {} not found", id);
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Skill.SKILL_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            SkillDTO skillDTOs = SkillConverter.toDTO(skillById.get());

            redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL, hashKeyForSkill, skillDTOs);

            return skillDTOs;

        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public PagingModel getAll(Integer page, Integer limit) throws BaseException {
        logger.info("Get all account with paging");
        try {
            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            String hashKeyForSkill = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL + "all:" + page + ":" + limit;

            List<SkillDTO> skillDTOs;

            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL, hashKeyForSkill)) {
                logger.info("Fetching skill from cache for page {} and limit {}", page, limit);
                skillDTOs = (List<SkillDTO>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL, hashKeyForSkill);
            } else {
                logger.info("Fetching account from database for page {} and limit {}", page, limit);
                List<Skill> skills = skillRepository.findAllByOrderByCreatedDate(pageable);
                skillDTOs = skills.stream().map(SkillConverter::toDTO).toList();
                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL, hashKeyForSkill, skillDTOs);
            }

            result.setListResult(skillDTOs);

            result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
            result.setLimit(limit);

            return result;
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    public int totalItem() {
        return (int) skillRepository.count();
    }

    public int totalItemWithStatusActive() {
        return skillRepository.countByStatus(ConstStatus.ACTIVE_STATUS);
    }

    @Override
    public PagingModel findAllByStatusTrue(Integer page, Integer limit) throws BaseException {
        logger.info("Get all account with paging");
        try {
            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            String hashKeyForSkill = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL + "all:" + "active:" + page + ":" + limit;

            List<SkillDTO> skillDTOs;

            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL, hashKeyForSkill)) {
                logger.info("Fetching skill from cache for page {} and limit {}", page, limit);
                skillDTOs = (List<SkillDTO>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL, hashKeyForSkill);
            } else {
                logger.info("Fetching account from database for page {} and limit {}", page, limit);
                List<Skill> skills = skillRepository.findAllByStatusOrderByCreatedDate(ConstStatus.ACTIVE_STATUS, pageable);
                skillDTOs = skills.stream().map(SkillConverter::toDTO).toList();
                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL, hashKeyForSkill, skillDTOs);
            }

            result.setListResult(skillDTOs);

            result.setTotalPage(((int) Math.ceil((double) (totalItemWithStatusActive()) / limit)));
            result.setLimit(limit);

            return result;
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public SkillDTO create(CreateSkillRequest request) throws BaseException {
        try {
            logger.info("Create skill");
            Skill skill = new Skill();
            skill.setName(request.getName());

            Major majorById = MajorConverter.toEntity(majorService.findById(request.getMajorId()));

            skill.setMajor(majorById);
            skill.setStatus(ConstStatus.ACTIVE_STATUS);

            skillRepository.save(skill);

            Set<String> keysToDelete = redisTemplate.keys("Skill:*");
            if (ValidateUtil.IsNotNullOrEmptyForSet(keysToDelete)) {
                redisTemplate.delete(keysToDelete);
            }

            return SkillConverter.toDTO(skill);

        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public Boolean update(UUID id, UpdateSkillRequest request) throws BaseException {
        try {
            logger.info("Update skill");
            Skill skillById = SkillConverter.toEntity(findById(id));

            skillById.setId(id);
            skillById.setName(request.getName());
            skillById.setStatus(request.getStatus());

            Major majorById = MajorConverter.toEntity(majorService.findById(request.getMajorId()));

            skillById.setMajor(majorById);

            skillRepository.save(skillById);

            Set<String> keysToDelete = redisTemplate.keys("Skill:*");
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
    public Boolean delete(UUID id) throws BaseException {
        return null;
    }

    @Override
    public Boolean changeStatus(UUID id) throws BaseException {
        try {
            logger.info("Find skill by id {}", id);
            Skill skillById = SkillConverter.toEntity(findById(id));

            if (skillById.getStatus().equals(ConstStatus.ACTIVE_STATUS)) {
                skillById.setStatus(ConstStatus.INACTIVE_STATUS);
            } else {
                skillById.setStatus(ConstStatus.ACTIVE_STATUS);
            }

            skillRepository.save(skillById);

            Set<String> keysToDelete = redisTemplate.keys("Skill:*");
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
    public List<AllSkillOfCompanyResponse> getAllSkillOfCompany(UUID id) throws BaseException {
        try {
            logger.info("Find all skill by company id {}", id);
            String hashKeyForSkill = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL + "companyId:" + id.toString();
            List<AllSkillOfCompanyResponse> skillDTOByRedis = (List<AllSkillOfCompanyResponse>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL, hashKeyForSkill);

            if (!Objects.isNull(skillDTOByRedis)) {
                return skillDTOByRedis;
            }

            Company company = CompanyConverter.toEntity(companyService.findById(id));

            List<AllSkillOfCompanyResponse> allSkillOfCompanyResponse = skillRepository.findDistinctSkillsByCompanyId(id);

            redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL, hashKeyForSkill, allSkillOfCompanyResponse);

            return allSkillOfCompanyResponse;
        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }
}
