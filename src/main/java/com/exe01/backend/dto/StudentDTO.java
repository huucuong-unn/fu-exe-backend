package com.exe01.backend.dto;

import com.exe01.backend.entity.Account;
import com.exe01.backend.entity.University;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO extends BaseDTO implements Serializable {

    private UUID id;

    private UniversityDTO university;

    private AccountDTO account;

    private String name;

    private Date dob;

    private String studentCode;

    private String status;

}
