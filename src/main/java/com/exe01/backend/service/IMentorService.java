package com.exe01.backend.service;

import com.exe01.backend.dto.MentorDTO;
import com.exe01.backend.dto.request.mentor.CreateMentorRequest;
import com.exe01.backend.dto.request.mentor.UpdateMentorRequest;
import com.exe01.backend.dto.response.mentorProfile.CreateMentorResponse;
import com.exe01.backend.dto.response.mentorProfile.MentorsResponse;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

public interface IMentorService extends IGenericService<MentorDTO>{

    CreateMentorResponse create(CreateMentorRequest request) throws BaseException;

    Boolean update(UUID id, UpdateMentorRequest request) throws BaseException;

    PagingModel getMentorsWithAllInformation(Integer page, Integer limit) throws BaseException;

    MentorsResponse getMentorByMentorProfileId(UUID id) throws BaseException;

    List<MentorsResponse> getMentorsByCompanyId(UUID id) throws BaseException;

    Boolean changeStatus(UUID id) throws BaseException;

    List<MentorsResponse> getAllSimillaryMentor(UUID companyId, UUID mentorId) throws BaseException;

    List<MentorsResponse> getAllMentorByStudentId(UUID id) throws BaseException;

    PagingModel getAllMentorForAdminSearch( UUID companyId, String mentorName,int page, int limit) throws BaseException;
}
