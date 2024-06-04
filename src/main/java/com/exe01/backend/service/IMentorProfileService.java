package com.exe01.backend.service;

import com.exe01.backend.dto.MentorProfileDTO;
import com.exe01.backend.dto.request.mentorProfile.CreateMentorProfileRequest;
import com.exe01.backend.dto.request.mentorProfile.UpdateMentorProfileRequest;
import com.exe01.backend.exception.BaseException;

import java.util.UUID;

public interface IMentorProfileService extends IGenericService<MentorProfileDTO> {

    public MentorProfileDTO create(CreateMentorProfileRequest request) throws BaseException;

    public Boolean update(UUID id, UpdateMentorProfileRequest request) throws BaseException;

    public Boolean delete(UUID id) throws BaseException;

}
