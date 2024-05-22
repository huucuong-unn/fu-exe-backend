package com.exe01.backend.service.impl;

import com.exe01.backend.constant.ConstStatus;
import com.exe01.backend.converter.GenericConverter;
import com.exe01.backend.converter.MentorConverter;
import com.exe01.backend.converter.MentorProfileConverter;
import com.exe01.backend.dto.MentorDTO;
import com.exe01.backend.dto.MentorProfileDTO;
import com.exe01.backend.dto.request.mentor.CreateMentorRequest;
import com.exe01.backend.dto.request.mentor.UpdateMentorRequest;
import com.exe01.backend.entity.Mentor;
import com.exe01.backend.entity.MentorProfile;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.repository.MentorProfileRepository;
import com.exe01.backend.repository.MentorRepository;
import com.exe01.backend.service.IMentorService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MentorServiceImpl implements IMentorService {

    Logger logger = LoggerFactory.getLogger(MentorServiceImpl.class);

    @Autowired
    GenericConverter genericConverter;

    @Autowired
    MentorRepository mentorRepository;

    @Autowired
    MentorProfileRepository mentorProfileRepository;

    @Override
    public MentorDTO findById(UUID id) {
        logger.info("Find mentor by id {}", id);
        var mentorById = mentorRepository.findById(id);
        boolean isMentorExist = mentorById.isPresent();

        if (!isMentorExist) {
            logger.warn("Mentor with id {} not found", id);
            throw new EntityNotFoundException();
        }

        return MentorConverter.toDto(mentorById.get());
    }

    @Override
    public PagingModel getAll(Integer page, Integer limit) {
        logger.info("Get all mentor");
        PagingModel result = new PagingModel();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);

        List<Mentor> mentors = mentorRepository.findAllByOrderByCreatedDate(pageable);

        List<MentorDTO> mentorProfileDTOs = mentors.stream().map(MentorConverter::toDto).toList();

        result.setListResult(mentorProfileDTOs);
        result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
        result.setLimit(limit);

        return result;
    }

    public int totalItem() {
        return (int) mentorRepository.count();
    }

    @Override
    public PagingModel findAllByStatusTrue(Integer page, Integer limit) {
        logger.info("Get all mentor with status is ACTIVE");
        PagingModel result = new PagingModel();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);

        List<Mentor> mentors = mentorRepository.findAllByStatusOrderByCreatedDate(ConstStatus.ACTIVE_STATUS, pageable);

        List<MentorDTO> mentorProfileDTOs = mentors.stream().map(MentorConverter::toDto).toList();

        result.setListResult(mentorProfileDTOs);
        result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
        result.setLimit(limit);

        return result;
    }

    @Override
    public MentorDTO create(CreateMentorRequest request) {
        logger.info("Create mentor");
        logger.info("Find mentor profile by id {}", request.getMentorProfileId());
        var mentorProfileById = mentorProfileRepository.findById(request.getMentorProfileId());
        boolean isMentorProfileExist = mentorProfileById.isPresent();

        if (!isMentorProfileExist) {
            logger.warn("Mentor profile with id {} not found", request.getMentorProfileId());
            throw new EntityNotFoundException();
        }

        Mentor mentor = new Mentor();
//        mentor.setMentorProfiles();
        return null;
    }

    @Override
    public Boolean update(UUID id, UpdateMentorRequest request) {
        return null;
    }

    @Override
    public Boolean delete(UUID id) {
        return null;
    }

}
