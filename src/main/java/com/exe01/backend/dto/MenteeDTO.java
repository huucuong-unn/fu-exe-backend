package com.exe01.backend.dto;

import com.exe01.backend.entity.MentorApply;
import com.exe01.backend.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenteeDTO extends BaseDTO{

    private StudentDTO student;

    private String status;


}
