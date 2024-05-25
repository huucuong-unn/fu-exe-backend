package com.exe01.backend.service;

import com.exe01.backend.dto.MentorDTO;
import com.exe01.backend.dto.request.mentor.CreateMentorRequest;
import com.exe01.backend.dto.request.mentor.UpdateMentorRequest;
import com.exe01.backend.dto.response.mentorProfile.CreateMentorResponse;

import java.util.UUID;

public interface IMentorService extends IGenericService<MentorDTO>{

    public CreateMentorResponse create(CreateMentorRequest request);

    public Boolean update(UUID id, UpdateMentorRequest request);

    public Boolean delete(UUID id);

}
