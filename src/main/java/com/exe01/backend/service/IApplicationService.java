package com.exe01.backend.service;

import com.exe01.backend.dto.ApplicationDTO;
import com.exe01.backend.dto.request.application.BaseApplicationRequest;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;

import java.util.UUID;

public interface IApplicationService extends IGenericService<ApplicationDTO> {

    ApplicationDTO create(BaseApplicationRequest request) throws BaseException;

    Boolean update(UUID id, BaseApplicationRequest request) throws BaseException;

    Boolean changeStatus(UUID id) throws BaseException;

    PagingModel findByMentorId(UUID mentorId, int page, int limit) throws BaseException;

    PagingModel findByMenteeId(UUID menteeId, int page, int limit) throws BaseException;

    void approveApplication(UUID applicationId) throws BaseException;


}
