package com.exe01.backend.converter;

import com.exe01.backend.dto.MentorProfileDTO;
import com.exe01.backend.entity.MentorProfile;

public class MentorProfileConverter {

    public static MentorProfileDTO toDto(MentorProfile mentorProfile) {
        MentorProfileDTO mentorProfileDTO = new MentorProfileDTO();
        mentorProfileDTO.setId(mentorProfile.getId());
        mentorProfileDTO.setProfilePicture(mentorProfile.getProfilePicture());
        mentorProfileDTO.setDescription(mentorProfile.getDescription());
        mentorProfileDTO.setShortDescription(mentorProfile.getShortDescription());
        mentorProfileDTO.setLinkedinUrl(mentorProfile.getLinkedinUrl());
        mentorProfileDTO.setRequirement(mentorProfile.getRequirement());
        mentorProfileDTO.setCreatedDate(mentorProfile.getCreatedDate());
        mentorProfileDTO.setModifiedDate(mentorProfile.getModifiedDate());
        mentorProfileDTO.setCreatedBy(mentorProfile.getCreatedBy());
        mentorProfileDTO.setModifiedBy(mentorProfile.getModifiedBy());
        mentorProfileDTO.setStatus(mentorProfile.getStatus());

        return mentorProfileDTO;
    }

}
