package com.exe01.backend.converter;

import com.exe01.backend.dto.StudentDTO;
import com.exe01.backend.entity.Student;
import com.exe01.backend.exception.BaseException;

public class StudentConverter {
    public static StudentDTO toDto(Student student) {

        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(student.getId());
        studentDTO.setName(student.getName());
        studentDTO.setStudentCode(student.getStudentCode());
        studentDTO.setStatus(student.getStatus());
        studentDTO.setDob(student.getDob());
        studentDTO.setAccount(AccountConverter.toDto(student.getAccount()));
        studentDTO.setUniversity(UniversityConverter.toDTO(student.getUniversity()));
        studentDTO.setCreatedDate(student.getCreatedDate());
        studentDTO.setModifiedDate(student.getModifiedDate());
        studentDTO.setModifiedBy(student.getModifiedBy());
        studentDTO.setCreatedBy(student.getCreatedBy());

        return studentDTO;

    }

    public static Student toEntity(StudentDTO studentDTO) throws BaseException {

        Student student = new Student();
        student.setId(studentDTO.getId());
        student.setName(studentDTO.getName());
        student.setStudentCode(studentDTO.getStudentCode());
        student.setStatus(studentDTO.getStatus());
        student.setDob(studentDTO.getDob());
        student.setAccount(AccountConverter.toEntity(studentDTO.getAccount()));
        student.setUniversity(UniversityConverter.toEntity(studentDTO.getUniversity()));
        student.setCreatedDate(studentDTO.getCreatedDate());
        student.setModifiedDate(studentDTO.getModifiedDate());
        student.setModifiedBy(studentDTO.getModifiedBy());
        student.setCreatedBy(studentDTO.getCreatedBy());

        return student;
    }

}
