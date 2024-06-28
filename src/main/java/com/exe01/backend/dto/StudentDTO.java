package com.exe01.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO extends BaseDTO implements Serializable {

    private String name;

    private Date dob;

    private String studentCode;

    private UniversityDTO university;

    private String frontStudentCard;

    private String backStudentCard;

    private AccountDTO account;

    private String status;

}
