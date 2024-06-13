package com.exe01.backend.service;

import com.exe01.backend.dto.MentorDTO;
import com.exe01.backend.dto.request.mentor.CreateMentorRequest;
import com.exe01.backend.dto.request.mentor.UpdateMentorRequest;
import com.exe01.backend.dto.response.mentorProfile.CreateMentorResponse;
import com.exe01.backend.dto.response.mentorProfile.MentorsResponse;
import com.exe01.backend.entity.Mentor;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;

import java.util.UUID;

public interface IMentorService extends IGenericService<MentorDTO>{

    CreateMentorResponse create(CreateMentorRequest request) throws BaseException;

    Boolean update(UUID id, UpdateMentorRequest request) throws BaseException;

    PagingModel getMentorsWithAllInformation(Integer page, Integer limit) throws BaseException;

    Boolean changeStatus(UUID id) throws BaseException;

}
