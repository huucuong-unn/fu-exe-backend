package com.exe01.backend.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class MentorDTO extends BaseDTO implements Serializable {

    private CompanyDTO company;

    private AccountDTO account;

    private String status;

    public MentorDTO(CompanyDTO company, AccountDTO account, String status) {
        this.company = company;
        this.account = account;
        this.status = status;
    }

}
