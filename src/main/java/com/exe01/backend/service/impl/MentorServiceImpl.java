package com.exe01.backend.service.impl;

import com.exe01.backend.constant.ConstHashKeyPrefix;
import com.exe01.backend.constant.ConstStatus;
import com.exe01.backend.converter.GenericConverter;
import com.exe01.backend.converter.MentorConverter;
import com.exe01.backend.converter.MentorProfileConverter;
import com.exe01.backend.dto.MentorDTO;
import com.exe01.backend.dto.MentorProfileDTO;
import com.exe01.backend.dto.request.mentor.CreateMentorRequest;
import com.exe01.backend.dto.request.mentor.UpdateMentorRequest;
import com.exe01.backend.dto.response.mentorProfile.CreateMentorResponse;
import com.exe01.backend.entity.Account;
import com.exe01.backend.entity.Mentor;
import com.exe01.backend.entity.MentorProfile;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.repository.AccountRepository;
import com.exe01.backend.repository.MentorProfileRepository;
import com.exe01.backend.repository.MentorRepository;
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
public class MentorServiceImpl implements IMentorService {

    Logger logger = LoggerFactory.getLogger(MentorServiceImpl.class);

    @Autowired
    GenericConverter genericConverter;

    @Autowired
    MentorRepository mentorRepository;

    @Autowired
    MentorProfileRepository mentorProfileRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public MentorDTO findById(UUID id) {
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
            throw new EntityNotFoundException();
        }

        String hashKeyForMentorProfile = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR_PROFILE + "all";
        List<MentorProfile> mentorProfilesByRedis = (List<MentorProfile>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR_PROFILE, hashKeyForMentorProfile);

        MentorDTO mentorDTO;


        if (mentorProfilesByRedis != null && !mentorProfilesByRedis.isEmpty()) {
            mentorDTO = MentorConverter.toDto(mentorById.get(), mentorProfilesByRedis);
        } else {
            logger.info("Find mentor profile list by mentor id {}", id);
            List<MentorProfile> mentorProfiles = mentorProfileRepository.findAllByMentorId(mentorById.get().getId());
            List<MentorProfileDTO> mentorProfileDTOs = mentorProfiles.stream().map(MentorProfileConverter::toDto).toList();
            mentorDTO = MentorConverter.toDto(mentorById.get(), mentorProfiles);
            redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR_PROFILE, hashKeyForMentorProfile, mentorProfileDTOs);
        }

        redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR, hashKeyForMentor, mentorDTO);

        return mentorDTO;
    }

    @Override
    public PagingModel getAll(Integer page, Integer limit) {
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
            mentorDTOs = mentors.stream().map(mentor -> {
                List<MentorProfile> mentorProfiles = mentorProfileRepository.findAllByMentorId(mentor.getId());
                return MentorConverter.toDto(mentor, mentorProfiles);
            }).toList();
            redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR, hashKeyForMentor, mentorDTOs);
        }

        result.setListResult(mentorDTOs);
        result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
        result.setLimit(limit);

        return result;
    }

    public int totalItem() {
        return (int) mentorRepository.count();
    }

    @Override
    public PagingModel findAllByStatusTrue(Integer page, Integer limit) {
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
            mentorDTOs = mentors.stream().map(mentor -> {
                List<MentorProfile> mentorProfiles = mentorProfileRepository.findAllByMentorId(mentor.getId());
                return MentorConverter.toDto(mentor, mentorProfiles);
            }).toList();
            redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_MENTOR, hashKeyForMentor, mentorDTOs);
        }

        result.setListResult(mentorDTOs);
        result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
        result.setLimit(limit);

        return result;
    }

    @Override
    public CreateMentorResponse create(CreateMentorRequest request) {
        logger.info("Create mentor");
        logger.info("Find account by id {}", request.getAccountId());
        Optional<Account> accountById = accountRepository.findById(request.getAccountId());
        boolean isAccountExist = accountById.isPresent();

        if (!isAccountExist) {
            logger.warn("Account with id {} not found", request.getAccountId());
            throw new EntityNotFoundException();
        }

        Mentor mentor = new Mentor();
        mentor.setAccount(accountById.get());
        mentor.setStatus(ConstStatus.ACTIVE_STATUS);

        Mentor saveMentor = mentorRepository.save(mentor);
        return MentorConverter.toCreateMentorResponse(saveMentor);
    }

    @Override
    public Boolean update(UUID id, UpdateMentorRequest request) {
        logger.info("Update mentor");
        logger.info("Find account by id {}", request.getAccountId());
        Optional<Account> accountById = accountRepository.findById(request.getAccountId());
        boolean isAccountExist = accountById.isPresent();

        if (!isAccountExist) {
            logger.warn("Account with id {} not found", request.getAccountId());
            throw new EntityNotFoundException();
        }

        Mentor mentor = new Mentor();
        mentor.setId(id);
        mentor.setAccount(accountById.get());
        mentor.setStatus(request.getStatus());

        mentorRepository.save(mentor);

        Set<String> keysToDelete = redisTemplate.keys("Mentor:*");
        if (keysToDelete != null && !keysToDelete.isEmpty()) {
            redisTemplate.delete(keysToDelete);
        }

        return true;
    }

    @Override
    public Boolean delete(UUID id) {
        logger.info("Delete mentor");
        logger.info("Find mentor by id {}", id);
        Optional<Mentor> mentorById = mentorRepository.findById(id);
        boolean mentorIsExist = mentorById.isPresent();

        if (!mentorIsExist) {
            logger.warn("Mentor profile with id {} not found", id);
            throw new EntityNotFoundException();
        }

        mentorById.get().setStatus(ConstStatus.INACTIVE_STATUS);

        mentorRepository.save(mentorById.get());

        Set<String> keysToDelete = redisTemplate.keys("Mentor:*");
        if (keysToDelete != null && !keysToDelete.isEmpty()) {
            redisTemplate.delete(keysToDelete);
        }
        return true;
    }

}
