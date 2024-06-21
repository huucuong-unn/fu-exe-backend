package com.exe01.backend.dto;

import lombok.*;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApplicationDTO extends BaseDTO implements Serializable {

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
    private String status;

}
