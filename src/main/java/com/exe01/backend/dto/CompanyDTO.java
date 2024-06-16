package com.exe01.backend.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
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

    private String overtimePolicy;

    private String status;

    public CompanyDTO(String name, String description, String country, String address,
                      String avatarUrl, String companyWebsiteUrl, String facebookUrl,
                      String workingTime, Integer companySize, String companyType,
                      String overtimePolicy, String status) {
        this.name = name;
        this.description = description;
        this.country = country;
        this.address = address;
        this.avatarUrl = avatarUrl;
        this.companyWebsiteUrl = companyWebsiteUrl;
        this.facebookUrl = facebookUrl;
        this.workingTime = workingTime;
        this.companySize = companySize;
        this.companyType = companyType;
        this.overtimePolicy = overtimePolicy;
        this.status = status;
    }

}
