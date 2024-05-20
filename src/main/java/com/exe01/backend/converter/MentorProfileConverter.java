package com.exe01.backend.converter;

import com.exe01.backend.dto.MentorProfiledDTO;
import com.exe01.backend.entity.MentorProfile;

public class MentorProfileConverter {

    public static MentorProfiledDTO toDto(MentorProfile mentorProfile) {
        MentorProfiledDTO mentorProfiledDTO = new MentorProfiledDTO();
        mentorProfiledDTO.setId(mentorProfile.getId());
        mentorProfiledDTO.setProfilePicture(mentorProfile.getProfilePicture());
        mentorProfiledDTO.setDescription(mentorProfile.getDescription());
        mentorProfiledDTO.setShortDescription(mentorProfile.getShortDescription());
        mentorProfiledDTO.setLinkedinUrl(mentorProfile.getLinkedinUrl());
        mentorProfiledDTO.setRequirement(mentorProfile.getRequirement());
        mentorProfiledDTO.setCreatedDate(mentorProfile.getCreatedDate());
        mentorProfiledDTO.setModifiedDate(mentorProfile.getModifiedDate());
        mentorProfiledDTO.setCreatedBy(mentorProfile.getCreatedBy());
        mentorProfiledDTO.setModifiedBy(mentorProfile.getModifiedBy());
        mentorProfiledDTO.setStatus(mentorProfile.getStatus());

        return mentorProfiledDTO;
    }

}
