package com.exe01.backend.service.impl;

import com.exe01.backend.converter.MentorProfileConverter;
import com.exe01.backend.converter.SkillConverter;
import com.exe01.backend.converter.SkillMentorProfileConverter;
import com.exe01.backend.dto.SkillMentorProfileDTO;
import com.exe01.backend.dto.request.skillMentorProfile.BaseSkillMentorProfileRequest;
import com.exe01.backend.entity.MentorProfile;
import com.exe01.backend.entity.Skill;
import com.exe01.backend.entity.SkillMentorProfile;
import com.exe01.backend.enums.ErrorCode;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.repository.SkillMentorProfileRepository;
import com.exe01.backend.service.ISkillMentorProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class SkillMentorProfileServiceImpl implements ISkillMentorProfileService {

    Logger logger = LoggerFactory.getLogger(SkillMentorProfileServiceImpl.class);

    @Autowired
    private SkillMentorProfileRepository skillMentorProfileRepository;

    @Autowired
    private MentorProfileServiceImpl mentorProfileService;

    @Autowired
    private SkillServiceImpl skillService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public SkillMentorProfileDTO findById(UUID id) throws BaseException {
        return null;
    }

    @Override
    public PagingModel getAll(Integer page, Integer limit) throws BaseException {
        return null;
    }

    @Override
    public PagingModel findAllByStatusTrue(Integer page, Integer limit) throws BaseException {
        return null;
    }

    @Override
    public SkillMentorProfileDTO create(BaseSkillMentorProfileRequest request) throws BaseException {
        try {
            logger.info("Create SkillMentorProfile");
            SkillMentorProfile skillMentorProfile = new SkillMentorProfile();

            MentorProfile mentorProfile = MentorProfileConverter.toEntity(mentorProfileService.findById(request.getMentorProfileId()));

            Skill skill = SkillConverter.toEntity(skillService.findById(request.getSkillId()));

            skillMentorProfile.setMentorProfile(mentorProfile);
            skillMentorProfile.setSkill(skill);
            skillMentorProfile.setSkillLevel(request.getSkillLevel());

            skillMentorProfileRepository.save(skillMentorProfile);

            Set<String> keysToDelete = redisTemplate.keys("SkillMentorProfile:*");
            if (keysToDelete != null && !keysToDelete.isEmpty()) {
                redisTemplate.delete(keysToDelete);
            }

            return SkillMentorProfileConverter.toDto(skillMentorProfile);

        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }
}
