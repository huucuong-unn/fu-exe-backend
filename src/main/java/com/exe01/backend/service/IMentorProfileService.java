package com.exe01.backend.service;

import com.exe01.backend.dto.MentorProfileDTO;
import com.exe01.backend.dto.request.mentorProfile.CreateMentorProfileRequest;
import com.exe01.backend.dto.request.mentorProfile.UpdateMentorProfileRequest;
import com.exe01.backend.dto.response.mentorProfile.MentorsResponse;
import com.exe01.backend.exception.BaseException;

import java.util.List;
import java.util.UUID;

public interface IMentorProfileService extends IGenericService<MentorProfileDTO> {

    MentorProfileDTO create(CreateMentorProfileRequest request) throws BaseException;

    Boolean update(UUID id, UpdateMentorProfileRequest request) throws BaseException;

    Boolean delete(UUID id) throws BaseException;

    Boolean changeStatus(UUID id) throws BaseException;
}
