package com.exe01.backend.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentorDTO extends BaseDTO implements Serializable {

    private CompanyDTO company;

    private AccountDTO account;

    private String status;

    private String fullName;

}
