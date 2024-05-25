package com.exe01.backend.service.impl;

import com.exe01.backend.constant.ConstStatus;
import com.exe01.backend.converter.GenericConverter;
import com.exe01.backend.converter.MentorProfileConverter;
import com.exe01.backend.dto.MentorProfileDTO;
import com.exe01.backend.dto.request.mentorProfile.CreateMentorProfileRequest;
import com.exe01.backend.dto.request.mentorProfile.UpdateMentorProfileRequest;
import com.exe01.backend.entity.Mentor;
import com.exe01.backend.entity.MentorProfile;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.repository.MentorProfileRepository;
import com.exe01.backend.repository.MentorRepository;
import com.exe01.backend.service.IMentorProfileService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MentorProfileServiceImpl implements IMentorProfileService {

    Logger logger = LoggerFactory.getLogger(MentorProfileServiceImpl.class);

    @Autowired
    GenericConverter genericConverter;

    @Autowired
    MentorProfileRepository mentorProfileRepository;

    @Autowired
    MentorRepository mentorRepository;

    @Override
    public MentorProfileDTO findById(UUID id) {
        logger.info("Find mentor profile by id {}", id);
        var mentorById = mentorProfileRepository.findById(id);
        boolean isMentorExist = mentorById.isPresent();

        if (!isMentorExist) {
            logger.warn("Mentor profile with id {} not found", id);
            throw new EntityNotFoundException();
        }

        return MentorProfileConverter.toDto(mentorById.get());
    }

    @Override
    public PagingModel getAll(Integer page, Integer limit) {
        logger.info("Get all mentor profile");
        PagingModel result = new PagingModel();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);

        List<MentorProfile> mentorProfiles = mentorProfileRepository.findAllByOrderByCreatedDate(pageable);

        List<MentorProfileDTO> mentorProfileDTOS = mentorProfiles.stream().map(MentorProfileConverter::toDto).toList();

        result.setListResult(mentorProfileDTOS);
        result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
        result.setLimit(limit);

        return result;
    }

    public int totalItem() {
        return (int) mentorProfileRepository.count();
    }

    @Override
    public PagingModel findAllByStatusTrue(Integer page, Integer limit) {
        logger.info("Get all mentor profile with status is ACTIVE");
        PagingModel result = new PagingModel();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);

        List<MentorProfile> mentorProfiles = mentorProfileRepository.findAllByStatusOrderByCreatedDate(ConstStatus.ACTIVE_STATUS, pageable);

        List<MentorProfileDTO> mentorProfileDTOS = mentorProfiles.stream().map(MentorProfileConverter::toDto).toList();

        result.setListResult(mentorProfileDTOS);
        result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
        result.setLimit(limit);

        return result;
    }

    @Override
    public MentorProfileDTO create(CreateMentorProfileRequest request) {
        logger.info("Create mentor profile");
        MentorProfile mentorProfile = new MentorProfile();

        Optional<Mentor> mentorById = mentorRepository.findById(request.getMentorId());
        boolean isMentorExist = mentorById.isPresent();

        if (!isMentorExist) {
            logger.warn("Mentor with id {} not found", request.getMentorId());
            throw new EntityNotFoundException();
        }

        mentorProfile.setMentor(mentorById.get());
        mentorProfile.setLinkedinUrl(request.getLinkedinUrl());
        mentorProfile.setRequirement(request.getRequirement());
        mentorProfile.setDescription(request.getDescription());
        mentorProfile.setShortDescription(request.getShortDescription());
        mentorProfile.setProfilePicture(request.getProfilePicture());
        mentorProfile.setStatus(ConstStatus.ACTIVE_STATUS);

        mentorProfileRepository.save(mentorProfile);

        return MentorProfileConverter.toDto(mentorProfile);
    }

    @Override
    public Boolean update(UUID id, UpdateMentorProfileRequest request) {
        logger.info("Update mentor profile with id {}", id);
        logger.info("Find mentor profile by id {}", id);
        var mentorProfileById = mentorProfileRepository.findById(id);
        boolean isMentorProfileExist = mentorProfileById.isPresent();

        if (!isMentorProfileExist) {
            logger.warn("Mentor profile with id {} not found", id);
            throw new EntityNotFoundException();
        }

        var mentorById = mentorRepository.findById(request.getMentorId());
        boolean isMentorExist = mentorById.isPresent();

        if (!isMentorExist) {
            logger.warn("Mentor with id {} not found", id);
            throw new EntityNotFoundException();
        }

        mentorProfileById.get().setId(id);
        mentorProfileById.get().setMentor(mentorById.get());
        mentorProfileById.get().setLinkedinUrl(request.getLinkedinUrl());
        mentorProfileById.get().setRequirement(request.getRequirement());
        mentorProfileById.get().setDescription(request.getDescription());
        mentorProfileById.get().setShortDescription(request.getShortDescription());
        mentorProfileById.get().setProfilePicture(request.getProfilePicture());

        mentorProfileRepository.save(mentorProfileById.get());

        return true;
    }

    @Override
    public Boolean delete(UUID id) {
        logger.info("Delete mentor profile with id {}", id);
        var mentorById = mentorProfileRepository.findById(id);
        boolean isMentorExist = mentorById.isPresent();

        if (!isMentorExist) {
            logger.warn("Mentor profile with id {} not found", id);
            throw new EntityNotFoundException();
        }

        mentorById.get().setId(id);
        mentorById.get().setStatus(ConstStatus.INACTIVE_STATUS);

        mentorProfileRepository.save(mentorById.get());

        return true;
    }
}
