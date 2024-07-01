package com.exe01.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentorProfileDTO extends BaseDTO implements Serializable {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MentorDTO mentorDTO;

    private String linkedinUrl;

    private String facebookUrl;

    private String googleMeetUrl;

    private String requirement;

    private String description;

    private String shortDescription;

    private String profilePicture;

    private String status;

    private String fullName;

}
