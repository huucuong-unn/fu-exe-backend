package com.exe01.backend.service;

import com.exe01.backend.dto.StudentDTO;
import com.exe01.backend.dto.request.major.CreateMajorRequest;
import com.exe01.backend.dto.request.major.UpdateMajorRequest;
import com.exe01.backend.dto.request.student.CreateStudentRequest;
import com.exe01.backend.dto.request.student.UpdateStudentRequest;

import java.util.UUID;

public interface IStudentService extends IGenericService<StudentDTO>{

    public Boolean update(UUID id, UpdateStudentRequest request);

    StudentDTO create(CreateStudentRequest request);

    public  Boolean delete(UUID id);

}
