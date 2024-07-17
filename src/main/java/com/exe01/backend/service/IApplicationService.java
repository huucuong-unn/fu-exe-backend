package com.exe01.backend.service;

import com.exe01.backend.dto.ApplicationDTO;
import com.exe01.backend.dto.Dashboard.MonthlyApplication;
import com.exe01.backend.dto.Dashboard.TopFiveCompany;
import com.exe01.backend.dto.request.application.BaseApplicationRequest;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface IApplicationService extends IGenericService<ApplicationDTO> {

    ApplicationDTO create(BaseApplicationRequest request) throws BaseException;

    Boolean update(UUID id, BaseApplicationRequest request) throws BaseException;

    Boolean changeStatus(UUID id) throws BaseException;

    PagingModel findByMentorId(UUID mentorId, int page, int limit) throws BaseException;

    PagingModel findByMenteeId(UUID menteeId, int page, int limit) throws BaseException;

    void approveApplication(UUID applicationId) throws BaseException;

    void rejectApplication(UUID applicationId, String message) throws BaseException;

    PagingModel findByMentorIdAndStatusAndSortByCreatedDate(UUID mentorId, String status, String createdDate, int page, int limit) throws BaseException;

    PagingModel findByStudentIdAndStatusAndSort(UUID studentId, UUID companyId, String mentorName, String status, String createdDate, int page, int limit) throws BaseException;

    @Async
    void uploadCvFile(UUID id, MultipartFile file) throws BaseException;

    List<MonthlyApplication> getApplicationByMonth() throws BaseException;

}
