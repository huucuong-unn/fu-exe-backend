package com.exe01.backend.service;

import com.exe01.backend.dto.StudentDTO;
import com.exe01.backend.dto.request.student.CreateStudentRequest;
import com.exe01.backend.dto.request.student.UpdateStudentRequest;
import com.exe01.backend.exception.BaseException;

import java.util.UUID;

public interface IStudentService extends IGenericService<StudentDTO> {

    Boolean update(UUID id, UpdateStudentRequest request) throws BaseException;

    StudentDTO create(CreateStudentRequest request) throws BaseException;

    Boolean delete(UUID id) throws BaseException;

    Boolean changeStatus(UUID id) throws BaseException;

    StudentDTO findByAccountId(UUID accountId) throws BaseException;

}
