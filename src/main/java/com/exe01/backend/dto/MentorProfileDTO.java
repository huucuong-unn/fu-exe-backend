package com.exe01.backend.dto;

import com.exe01.backend.dto.response.mentorProfile.FindMentorProfileByIdResponse;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
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

    public MentorProfileDTO(MentorDTO mentorDTO, String linkedinUrl, String facebookUrl,
                            String googleMeetUrl, String requirement, String description,
                            String shortDescription, String profilePicture, String status) {
        this.mentorDTO = mentorDTO;
        this.linkedinUrl = linkedinUrl;
        this.facebookUrl = facebookUrl;
        this.googleMeetUrl = googleMeetUrl;
        this.requirement = requirement;
        this.description = description;
        this.shortDescription = shortDescription;
        this.profilePicture = profilePicture;
        this.status = status;
    }

}
