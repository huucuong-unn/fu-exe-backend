package com.exe01.backend.service.impl;

import com.exe01.backend.constant.ConstStatus;
import com.exe01.backend.converter.GenericConverter;
import com.exe01.backend.converter.MajorConverter;
import com.exe01.backend.dto.MajorDTO;
import com.exe01.backend.dto.request.major.CreateMajorRequest;
import com.exe01.backend.dto.request.major.UpdateMajorRequest;
import com.exe01.backend.entity.Major;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.repository.MajorRepository;
import com.exe01.backend.service.IMajorService;
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
public class MajorServiceImpl implements IMajorService {

    Logger logger = LoggerFactory.getLogger(MajorServiceImpl.class);

    @Autowired
    GenericConverter genericConverter;

    @Autowired
    private MajorRepository majorRepository;

    @Override
    public MajorDTO findById(UUID id) {
        logger.info("Find major by id {}", id);
        var majorById = majorRepository.findById(id);
        boolean isMajorExist = majorById.isPresent();

        if (!isMajorExist) {
            //TODO
            logger.warn("Major with id {} not found", id);
            throw new EntityNotFoundException();
        }

        return MajorConverter.toDTO(majorById.get());
    }

    @Override
    public PagingModel getAll(Integer page, Integer limit) {
        logger.info("Get all major");
        PagingModel result = new PagingModel();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);

        List<Major> majors = majorRepository.findAllByOrderByCreatedDate(pageable);


        List<MajorDTO> majorDTOs = majors.stream().map(MajorConverter::toDTO).toList();

        result.setListResult(majorDTOs);

        result.setListResult(majorDTOs);
        result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
        result.setLimit(limit);

        return result;
    }

    public int totalItem() {
        return (int) majorRepository.count();
    }

    @Override
    public PagingModel findAllByStatusTrue(Integer page, Integer limit) {
        logger.info("Get all major with status is ACTIVE");
        PagingModel result = new PagingModel();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);

        List<Major> majors = majorRepository.findAllByStatusOrderByCreatedDate(ConstStatus.ACTIVE_STATUS, pageable);

        List<MajorDTO> majorDTOs = majors.stream().map(MajorConverter::toDTO).toList();

        result.setListResult(majorDTOs);
        result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
        result.setLimit(limit);

        return result;
    }

    @Override
    public MajorDTO create(CreateMajorRequest request) {
        logger.info("Create major");
        Major major = new Major();
        major.setStatus(ConstStatus.ACTIVE_STATUS);
        major.setName(request.getName());
        major.setDescription(request.getDescription());

        majorRepository.save(major);

        return MajorConverter.toDTO(major);
    }

    @Override
    public Boolean update(UUID id, UpdateMajorRequest request) {
        logger.info("Update major with id {}", id);
        logger.info("Find major by id {}", id);
        var majorById = majorRepository.findById(id);
        boolean isMajorExist = majorById.isPresent();

        if (!isMajorExist) {
            logger.warn("Major with id {} not found", id);
            throw new EntityNotFoundException();
        }

        majorById.get().setId(id);
        majorById.get().setName(request.getName());
        majorById.get().setDescription(request.getDescription());
        majorById.get().setStatus(request.getStatus());

        return true;
    }

    @Override
    public Boolean delete(UUID id) {
        logger.info("Delete major with id {}", id);
        var majorById = majorRepository.findById(id);
        boolean isMajorExist = majorById.isPresent();

        if (!isMajorExist) {
            logger.warn("Major with id {} not found", id);
            throw new EntityNotFoundException();
        }

        majorById.get().setStatus(ConstStatus.INACTIVE_STATUS);

        majorRepository.save(majorById.get());

        return true;
    }
}
