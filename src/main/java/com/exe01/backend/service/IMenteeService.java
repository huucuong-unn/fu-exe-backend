package com.exe01.backend.service;

import com.exe01.backend.dto.MenteeDTO;
import com.exe01.backend.dto.request.mentee.MenteeRequest;
import com.exe01.backend.exception.BaseException;

import java.util.Optional;
import java.util.UUID;

public interface IMenteeService extends IGenericService<MenteeDTO> {

    MenteeDTO create(MenteeRequest menteeRequest) throws BaseException;

    Boolean changeStatus(UUID id) throws BaseException;

   Optional<MenteeDTO> findByStudentId(UUID studentId) throws BaseException;

   int countAllByMentorId(UUID mentorId) throws BaseException;
}
