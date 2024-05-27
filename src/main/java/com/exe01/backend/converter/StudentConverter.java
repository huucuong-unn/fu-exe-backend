package com.exe01.backend.converter;

import com.exe01.backend.dto.RoleDTO;
import com.exe01.backend.dto.StudentDTO;
import com.exe01.backend.entity.Role;
import com.exe01.backend.entity.Student;

public class StudentConverter {
    public static StudentDTO toDto(Student student) {

        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(student.getId());
        studentDTO.setName(student.getName());
        studentDTO.setStudentCode(student.getStudentCode());
        studentDTO.setStatus(student.getStatus());
        studentDTO.setDob(student.getDob());
        studentDTO.setAccount(AccountConverter.toDto(student.getAccount()));
//TODO:        studentDTO.setUniversity(UniversityConverter.toDto(student.getUniversity()));
        studentDTO.setCreatedDate(student.getCreatedDate());
        studentDTO.setModifiedDate(student.getModifiedDate());
        studentDTO.setModifiedBy(student.getModifiedBy());
        studentDTO.setCreatedBy(student.getCreatedBy());

        return studentDTO;

    }

}
