package com.exe01.backend.converter;

import com.exe01.backend.dto.ApplicationDTO;
import com.exe01.backend.entity.Application;
import com.exe01.backend.exception.BaseException;

public class ApplicationConverter {

    public static ApplicationDTO toDto(Application application) {
        if (application == null) {
            return null;
        }

        ApplicationDTO applicationDto = new ApplicationDTO();
        applicationDto.setId(application.getId());
        applicationDto.setMentor(MentorConverter.toDto(application.getMentor()));
        applicationDto.setFullName(application.getFullName());
        applicationDto.setUserAddress(application.getUserAddress());
        applicationDto.setEmail(application.getEmail());
        applicationDto.setPhoneNumber(application.getPhoneNumber());
        applicationDto.setFacebookUrl(application.getFacebookUrl());
        applicationDto.setZaloAccount(application.getZaloAccount());
        applicationDto.setReasonApply(application.getReasonApply());
        applicationDto.setIntroduce(application.getIntroduce());
        applicationDto.setCvFile(application.getCvFile());
        applicationDto.setStudent(StudentConverter.toDto(application.getStudent()));
        applicationDto.setCreatedDate(application.getCreatedDate());
        applicationDto.setModifiedDate(application.getModifiedDate());
        applicationDto.setCreatedBy(application.getCreatedBy());
        applicationDto.setModifiedBy(application.getModifiedBy());
        applicationDto.setStatus(application.getStatus());
        return applicationDto;
    }

    public static Application toEntity(ApplicationDTO applicationDto) throws BaseException {
        if (applicationDto == null) {
            return null;
        }

        Application application = new Application();
        application.setId(applicationDto.getId());
        application.setMentor(MentorConverter.toEntity(applicationDto.getMentor()));
        application.setFullName(applicationDto.getFullName());
        application.setUserAddress(applicationDto.getUserAddress());
        application.setEmail(applicationDto.getEmail());
        application.setPhoneNumber(applicationDto.getPhoneNumber());
        application.setFacebookUrl(applicationDto.getFacebookUrl());
        application.setZaloAccount(applicationDto.getZaloAccount());
        application.setReasonApply(applicationDto.getReasonApply());
        application.setIntroduce(applicationDto.getIntroduce());
        application.setCvFile(applicationDto.getCvFile());
        application.setCreatedDate(applicationDto.getCreatedDate());
        application.setModifiedDate(applicationDto.getModifiedDate());
        application.setCreatedBy(applicationDto.getCreatedBy());
        application.setModifiedBy(applicationDto.getModifiedBy());
        application.setStudent(StudentConverter.toEntity(applicationDto.getStudent()));
        application.setStatus(applicationDto.getStatus());

        return application;
    }
}
