package com.exe01.backend.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentorProfileDTO extends BaseDTO implements Serializable {

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
