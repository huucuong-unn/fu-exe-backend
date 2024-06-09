package com.exe01.backend.dto;

import com.exe01.backend.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApplicationDTO extends BaseDTO{

    private MentorDTO mentor;
    private String fullName;
    private String userAddress;
    private String email;
    private String phoneNumber;
    private String job;
    private String facebookUrl;
    private String zaloAccount;
    private String reasonApply;
    private String introduce;
    private String cvFile;
    private StudentDTO student;

}
