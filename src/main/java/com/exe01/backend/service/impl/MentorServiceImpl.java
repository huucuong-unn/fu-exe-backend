package com.exe01.backend.service.impl;

import com.exe01.backend.constant.ConstError;
import com.exe01.backend.constant.ConstHashKeyPrefix;
import com.exe01.backend.constant.ConstStatus;
import com.exe01.backend.converter.*;
import com.exe01.backend.dto.MentorDTO;
import com.exe01.backend.dto.SkillMentorProfileDTO;
import com.exe01.backend.dto.request.mentor.CreateMentorRequest;
import com.exe01.backend.dto.request.mentor.UpdateMentorRequest;
import com.exe01.backend.dto.response.mentorProfile.CreateMentorResponse;
import com.exe01.backend.dto.response.mentorProfile.MentorsResponse;
import com.exe01.backend.entity.Account;
import com.exe01.backend.entity.Company;
import com.exe01.backend.entity.Mentor;
import com.exe01.backend.entity.MentorProfile;
import com.exe01.backend.enums.ErrorCode;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.repository.MentorProfileRepository;
import com.exe01.backend.repository.MentorRepository;
import com.exe01.backend.repository.SkillMentorProfileRepository;
import com.exe01.backend.service.IAccountService;
import com.exe01.backend.service.ICompanyService;
import com.exe01.backend.service.IMenteeService;
import com.exe01.backend.service.IMentorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MentorServiceImpl implements IMentorService {

    Logger logger = LoggerFactory.getLogger(MentorServiceImpl.class);

    @Autowired
    MentorRepository mentorRepository;

    @Autowired
    @Lazy
    IMenteeService menteeService;

    @Autowired
    MentorProfileRepository mentorProfileRepository;

    @Autowired
    SkillMentorProfileRepository skillMentorProfileRepository;

    @Autowired
    IAccountService accountService;

    @Autowired
    ICompanyService companyService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public MentorDTO findById(UUID id) throws BaseException {

        try {
            logger.info("Find mentor by id {}", id);
            String hashKeyForMentor = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR + id.toString();
            MentorDTO mentorDTOByRedis = (MentorDTO) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR, hashKeyForMentor);

            if (!Objects.isNull(mentorDTOByRedis)) {
                return mentorDTOByRedis;
            }

            Optional<Mentor> mentorById = mentorRepository.findById(id);
            boolean isMentorExist = mentorById.isPresent();

            if (!isMentorExist) {
                logger.warn("Mentor with id {} not found", id);
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Mentor.MENTOR_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            logger.info("Find mentor profile list by mentor id {}", id);
            MentorDTO mentorDTO = MentorConverter.toDto(mentorById.get());

            redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR, hashKeyForMentor, mentorDTO);

            return mentorDTO;
        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException; // rethrow the original BaseException
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }

    }

    @Override
    public PagingModel getMentorsWithAllInformation(Integer page, Integer limit) throws BaseException {
        try {

            logger.info("Get all mentor with all information");
            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            String hashKeyForMentor = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL_MENTOR_PROFILE + "all:" + "information:" + page + ":" + limit;

            List<MentorsResponse> mentorDTOs;

            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL_MENTOR_PROFILE, hashKeyForMentor)) {
                logger.info("Fetching mentors from cache for page {} and limit {}", page, limit);
                mentorDTOs = (List<MentorsResponse>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL_MENTOR_PROFILE, hashKeyForMentor);
            } else {
                logger.info("Fetching mentors from database for page {} and limit {}", page, limit);
                List<MentorProfile> mentorProfiles = mentorProfileRepository.findAllBy(pageable);
                mentorDTOs = new ArrayList<>();
                for (MentorProfile mentorProfile : mentorProfiles) {
                    MentorsResponse mentorsResponse = new MentorsResponse();
                    mentorsResponse.setMentorProfile(MentorProfileConverter.toDto(mentorProfile));
                    List<SkillMentorProfileDTO> skillDTOs = skillMentorProfileRepository.findAllByMentorProfileId(mentorProfile.getId())
                            .stream()
                            .map(SkillMentorProfileConverter::toDto)
                            .toList();
                    mentorsResponse.setSkills(skillDTOs);
                    mentorDTOs.add(mentorsResponse);
                }

                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL_MENTOR_PROFILE, hashKeyForMentor, mentorDTOs);
            }

            result.setListResult(mentorDTOs);
            result.setTotalPage(((int) Math.ceil((double) (totalItemByStatusUsing()) / limit)));
            result.setTotalCount(totalItemByStatusUsing());
            result.setLimit(limit);

            return result;
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    private int totalItemByStatusUsing() {
        return (int) mentorProfileRepository.countByStatus("USING");
    }

    @Override
    public MentorsResponse getMentorByMentorProfileId(UUID id) throws BaseException {
        try {
            logger.info("Find mentor by id {}", id);
            String hashKeyForMentor = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL_MENTOR_PROFILE + id.toString();
            MentorsResponse mentorDTOByRedis = (MentorsResponse) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL_MENTOR_PROFILE, hashKeyForMentor);

            if (!Objects.isNull(mentorDTOByRedis)) {
                return mentorDTOByRedis;
            }

            Optional<MentorProfile> mentorProfileById = mentorProfileRepository.findById(id);
            boolean isMentorProfileExits = mentorProfileById.isPresent();

            if (!isMentorProfileExits) {
                logger.warn("Mentor profile with id {} not found", id);
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Mentor.MENTOR_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            List<SkillMentorProfileDTO> skillDTOs = skillMentorProfileRepository.findAllByMentorProfileId(id)
                    .stream()
                    .map(SkillMentorProfileConverter::toDto)
                    .toList();

            MentorsResponse mentorsResponse = new MentorsResponse();
            mentorsResponse.setMentorProfile(MentorProfileConverter.toDto(mentorProfileById.get()));
            mentorsResponse.setSkills(skillDTOs);

            redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL_MENTOR_PROFILE, hashKeyForMentor, mentorsResponse);

            return mentorsResponse;

        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException; // rethrow the original BaseException
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public List<MentorsResponse> getMentorsByCompanyId(UUID id) throws BaseException {
        try {
            logger.info("Find mentors by company id {}", id);
            String hashKeyForMentor = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL_MENTOR_PROFILE + "companyId:" + id.toString();
            List<MentorsResponse> mentorsDTOByRedis = (List<MentorsResponse>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL_MENTOR_PROFILE, hashKeyForMentor);

            if (!Objects.isNull(mentorsDTOByRedis)) {
                return mentorsDTOByRedis;
            }

            List<MentorProfile> mentorProfiles = mentorProfileRepository.findByCompanyId(id);

            List<MentorsResponse> mentorsResponses = new ArrayList<>();

            for (MentorProfile mentorProfile : mentorProfiles) {
                MentorsResponse mentorsResponse = new MentorsResponse();
                mentorsResponse.setMentorProfile(MentorProfileConverter.toDto(mentorProfile));

                List<SkillMentorProfileDTO> skillDTOs = skillMentorProfileRepository.findAllByMentorProfileId(mentorProfile.getId())
                        .stream()
                        .map(SkillMentorProfileConverter::toDto)
                        .toList();

                mentorsResponse.setSkills(skillDTOs);

                mentorsResponses.add(mentorsResponse);
            }

            redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL_MENTOR_PROFILE, hashKeyForMentor, mentorsResponses);

            return mentorsResponses;
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public PagingModel getAll(Integer page, Integer limit) throws BaseException {
        try {

            logger.info("Get all mentor");
            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            String hashKeyForMentor = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR + "all:" + page + ":" + limit;

            List<MentorDTO> mentorDTOs;

            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR, hashKeyForMentor)) {
                logger.info("Fetching mentors from cache for page {} and limit {}", page, limit);
                mentorDTOs = (List<MentorDTO>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR, hashKeyForMentor);
            } else {
                logger.info("Fetching mentors from database for page {} and limit {}", page, limit);
                List<Mentor> mentors = mentorRepository.findAllByOrderByCreatedDate(pageable);
                mentorDTOs = mentors.stream().map(MentorConverter::toDto).toList();
                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR, hashKeyForMentor, mentorDTOs);
            }

            result.setListResult(mentorDTOs);
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

    public int totalItem() {
        return (int) mentorRepository.count();
    }

    @Override
    public PagingModel findAllByStatusTrue(Integer page, Integer limit) throws BaseException {

        try {
            logger.info("Get all mentor with status active");
            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            String hashKeyForMentor = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR + "all:" + "active:" + page + ":" + limit;

            List<MentorDTO> mentorDTOs;

            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR, hashKeyForMentor)) {
                logger.info("Fetching mentors from cache for page {} and limit {}", page, limit);
                mentorDTOs = (List<MentorDTO>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR, hashKeyForMentor);
            } else {
                logger.info("Fetching mentors from database for page {} and limit {}", page, limit);
                List<Mentor> mentors = mentorRepository.findAllByStatusOrderByCreatedDate(ConstStatus.ACTIVE_STATUS, pageable);
                mentorDTOs = mentors.stream().map(MentorConverter::toDto).toList();
                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR, hashKeyForMentor, mentorDTOs);
            }

            result.setListResult(mentorDTOs);
            result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
            result.setLimit(limit);

            return result;
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public CreateMentorResponse create(CreateMentorRequest request) throws BaseException {

        try {

            logger.info("Create mentor");
            logger.info("Find account by id {}", request.getAccountId());
            Account account = AccountConverter.toEntity(accountService.findById(request.getAccountId()));
            Company company = CompanyConverter.toEntity(companyService.findById(request.getCompanyId()));
            Mentor mentor = new Mentor();
            mentor.setAccount(account);
            mentor.setCompany(company);
            mentor.setStatus(ConstStatus.ACTIVE_STATUS);

            Mentor saveMentor = mentorRepository.save(mentor);
            return MentorConverter.toCreateMentorResponse(saveMentor);
        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException; // rethrow the original BaseException
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public Boolean update(UUID id, UpdateMentorRequest request) throws BaseException {

        try {

            logger.info("Update mentor");
            logger.info("Find account by id {}", request.getAccountId());
            Account account = AccountConverter.toEntity(accountService.findById(request.getAccountId()));

            Mentor mentor = MentorConverter.toEntity(findById(id));

            mentor.setAccount(account);

            mentorRepository.save(mentor);

            Set<String> keysToDelete = redisTemplate.keys("Mentor:*");
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
            logger.info("Change status mentee with id {}", id);
            Mentor mentor = MentorConverter.toEntity(findById(id));

            if (mentor.getStatus().equals(ConstStatus.ACTIVE_STATUS)) {
                mentor.setStatus(ConstStatus.INACTIVE_STATUS);
            } else {
                mentor.setStatus(ConstStatus.ACTIVE_STATUS);
            }

            mentorRepository.save(mentor);

            Set<String> keysToDelete = redisTemplate.keys("Mentor:*");
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
    public List<MentorsResponse> getAllSimillaryMentor(UUID companyId, UUID mentorId) throws BaseException {
        try {

            logger.info("Get all mentor with all information");

            String hashKeyForMentor = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL_MENTOR_PROFILE + "all:" + "information:" + "similar" + mentorId;

            List<MentorsResponse> mentorDTOs;

            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL_MENTOR_PROFILE, hashKeyForMentor)) {
                mentorDTOs = (List<MentorsResponse>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL_MENTOR_PROFILE, hashKeyForMentor);
            } else {
                List<MentorProfile> mentorProfiles = mentorProfileRepository.findAllByMentorProfilesStatusAndCompanyId(companyId, mentorId, ConstStatus.CampaignStatus.CLOSED, ConstStatus.MentorProfileStatus.USING);
                mentorDTOs = new ArrayList<>();
                for (MentorProfile mentorProfile : mentorProfiles) {
                    MentorsResponse mentorsResponse = new MentorsResponse();
                    mentorsResponse.setMentorProfile(MentorProfileConverter.toDto(mentorProfile));
                    List<SkillMentorProfileDTO> skillDTOs = skillMentorProfileRepository.findAllByMentorProfileId(mentorProfile.getId())
                            .stream()
                            .map(SkillMentorProfileConverter::toDto)
                            .toList();
                    mentorsResponse.setSkills(skillDTOs);
                    mentorDTOs.add(mentorsResponse);
                }

                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL_MENTOR_PROFILE, hashKeyForMentor, mentorDTOs);
            }

            return mentorDTOs;

        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public List<MentorsResponse> getAllMentorByStudentId(UUID id) throws BaseException {
        try {

            logger.info("Get all mentor with all information");

            String hashKeyForMentor = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL_MENTOR_PROFILE + "all:" + "information:" + "similar" + id;

            List<MentorsResponse> mentorDTOs;

            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL_MENTOR_PROFILE, hashKeyForMentor)) {
                mentorDTOs = (List<MentorsResponse>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL_MENTOR_PROFILE, hashKeyForMentor);
            } else {
                List<MentorProfile> mentorProfiles = mentorProfileRepository.findAllByMenteeId(id);
                mentorDTOs = new ArrayList<>();
                for (MentorProfile mentorProfile : mentorProfiles) {
                    MentorsResponse mentorsResponse = new MentorsResponse();
                    mentorsResponse.setMentorProfile(MentorProfileConverter.toDto(mentorProfile));
                    List<SkillMentorProfileDTO> skillDTOs = skillMentorProfileRepository.findAllByMentorProfileId(mentorProfile.getId())
                            .stream()
                            .map(SkillMentorProfileConverter::toDto)
                            .toList();
                    mentorsResponse.setSkills(skillDTOs);
                    mentorDTOs.add(mentorsResponse);
                }

                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL_MENTOR_PROFILE, hashKeyForMentor, mentorDTOs);
            }

            return mentorDTOs;

        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public PagingModel getAllMentorForAdminSearch(UUID companyId, String mentorName, int page, int limit) throws BaseException {
        try {

            logger.info("Get all mentor");
            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            logger.info("Get all mentor with all information");

            String hashKeyForMentor = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL_MENTOR_PROFILE + "all:" + mentorName  + companyId + page + limit;

            List<MentorsResponse> mentorDTOs;

            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL_MENTOR_PROFILE, hashKeyForMentor)) {
                mentorDTOs = (List<MentorsResponse>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL_MENTOR_PROFILE, hashKeyForMentor);
            } else {
                List<MentorProfile> mentorProfiles = mentorProfileRepository.findAllByMentorProfilesForAdminSearch(companyId, mentorName, pageable);
                mentorDTOs = new ArrayList<>();
                for (MentorProfile mentorProfile : mentorProfiles) {
                    MentorsResponse mentorsResponse = new MentorsResponse();
                    mentorsResponse.setMentorProfile(MentorProfileConverter.toDto(mentorProfile));
                    List<SkillMentorProfileDTO> skillDTOs = skillMentorProfileRepository.findAllByMentorProfileId(mentorProfile.getId())
                            .stream()
                            .map(SkillMentorProfileConverter::toDto)
                            .toList();
                    mentorsResponse.setSkills(skillDTOs);
                    mentorsResponse.setTotalMentees(menteeService.countAllByMentorId(mentorProfile.getMentor().getId()));
                    mentorDTOs.add(mentorsResponse);
                }

                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_SKILL_MENTOR_PROFILE, hashKeyForMentor, mentorDTOs);
            }


            result.setListResult(mentorDTOs);
            result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
            result.setLimit(limit);
            return result;

        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());


        }
    }
}
