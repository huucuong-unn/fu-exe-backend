package com.exe01.backend.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyDTO extends BaseDTO implements Serializable {

    private String name;

    private String description;

    private String country;

    private String address;

    private String avatarUrl;

    private String companyWebsiteUrl;

    private String facebookUrl;

    private String workingTime;

    private Integer companySize;

    private String companyType;

    private String status;

    private AccountDTO account;

}
