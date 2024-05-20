package com.exe01.backend.service;

import com.exe01.backend.dto.MentorProfiledDTO;
import com.exe01.backend.dto.request.mentorProfile.CreateMentorProfileRequest;
import com.exe01.backend.dto.request.mentorProfile.UpdateMentorProfileRequest;

import java.util.UUID;

public interface IMentorProfileService extends IGenericService<MentorProfiledDTO> {

    public MentorProfiledDTO create(CreateMentorProfileRequest request);

    public Boolean update(UUID id, UpdateMentorProfileRequest request);

    public Boolean delete(UUID id);

}
