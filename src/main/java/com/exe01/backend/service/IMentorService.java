package com.exe01.backend.service;

import com.exe01.backend.dto.MentorDTO;
import com.exe01.backend.dto.request.mentor.CreateMentorRequest;
import com.exe01.backend.dto.request.mentor.UpdateMentorRequest;
import com.exe01.backend.dto.response.mentorProfile.CreateMentorResponse;

import java.util.UUID;

public interface IMentorService extends IGenericService<MentorDTO>{

    CreateMentorResponse create(CreateMentorRequest request);

    Boolean update(UUID id, UpdateMentorRequest request);

    Boolean delete(UUID id);

}
