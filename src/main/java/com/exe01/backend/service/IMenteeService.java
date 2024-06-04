package com.exe01.backend.service;

import com.exe01.backend.dto.MenteeDTO;
import com.exe01.backend.dto.MentorDTO;
import com.exe01.backend.dto.request.Mentee.MenteeRequest;
import com.exe01.backend.dto.request.mentor.CreateMentorRequest;
import com.exe01.backend.dto.request.mentor.UpdateMentorRequest;
import com.exe01.backend.dto.response.mentorProfile.CreateMentorResponse;
import com.exe01.backend.entity.Mentee;
import com.exe01.backend.exception.BaseException;

import java.util.UUID;

public interface IMenteeService extends IGenericService<MenteeDTO>{

    MenteeDTO create(MenteeRequest menteeRequest) throws BaseException;

    Boolean update(UUID id, MenteeRequest menteeRequest) throws BaseException;

    Boolean changeStatus(UUID id) throws BaseException;



}
