package com.exe01.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MentorProfiledDTO extends BaseDTO{

    private String linkedinUrl;

    private String requirement;

    private String description;

    private String shortDescription;

    private String profilePicture;

    private String status;

}
