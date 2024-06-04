package com.exe01.backend.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyDTO extends BaseDTO implements Serializable {

    private String name;

    private String country;

    private String address;

    private String avatarUrl;

    private String img;

    private String keySkill;

    private String topReason;

    private String url;

    private String workingTime;

    private Integer companySize;

    private String companyType;

    private String overtimePolicy;

    private String status;

}
