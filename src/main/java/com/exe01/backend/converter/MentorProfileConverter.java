package com.exe01.backend.converter;

import com.exe01.backend.dto.MentorProfileDTO;
import com.exe01.backend.entity.MentorProfile;
import com.exe01.backend.exception.BaseException;

public class MentorProfileConverter {

    public static MentorProfileDTO toDto(MentorProfile mentorProfile) {
        MentorProfileDTO mentorProfileDTO = new MentorProfileDTO();
        mentorProfileDTO.setId(mentorProfile.getId());
        mentorProfileDTO.setProfilePicture(mentorProfile.getProfilePicture());
        mentorProfileDTO.setDescription(mentorProfile.getDescription());
        mentorProfileDTO.setShortDescription(mentorProfile.getShortDescription());
        mentorProfileDTO.setLinkedinUrl(mentorProfile.getLinkedinUrl());
        mentorProfileDTO.setFacebookUrl(mentorProfile.getFacebookUrl());
        mentorProfileDTO.setGoogleMeetUrl(mentorProfile.getGoogleMeetUrl());
        mentorProfileDTO.setRequirement(mentorProfile.getRequirement());
        mentorProfileDTO.setCreatedDate(mentorProfile.getCreatedDate());
        mentorProfileDTO.setModifiedDate(mentorProfile.getModifiedDate());
        mentorProfileDTO.setCreatedBy(mentorProfile.getCreatedBy());
        mentorProfileDTO.setModifiedBy(mentorProfile.getModifiedBy());
        mentorProfileDTO.setStatus(mentorProfile.getStatus());
        mentorProfileDTO.setMentorDTO(MentorConverter.toDto(mentorProfile.getMentor()));

        return mentorProfileDTO;
    }

    public static MentorProfile toEntity(MentorProfileDTO mentorProfileDTO) throws BaseException {
        MentorProfile mentorProfile = new MentorProfile();
        mentorProfile.setId(mentorProfileDTO.getId());
        mentorProfile.setProfilePicture(mentorProfileDTO.getProfilePicture());
        mentorProfile.setDescription(mentorProfileDTO.getDescription());
        mentorProfile.setShortDescription(mentorProfileDTO.getShortDescription());
        mentorProfile.setLinkedinUrl(mentorProfileDTO.getLinkedinUrl());
        mentorProfile.setFacebookUrl(mentorProfileDTO.getFacebookUrl());
        mentorProfile.setGoogleMeetUrl(mentorProfileDTO.getGoogleMeetUrl());
        mentorProfile.setRequirement(mentorProfileDTO.getRequirement());
        mentorProfile.setCreatedDate(mentorProfileDTO.getCreatedDate());
        mentorProfile.setModifiedDate(mentorProfileDTO.getModifiedDate());
        mentorProfile.setCreatedBy(mentorProfileDTO.getCreatedBy());
        mentorProfile.setModifiedBy(mentorProfileDTO.getModifiedBy());
        mentorProfile.setStatus(mentorProfileDTO.getStatus());
        mentorProfile.setMentor(MentorConverter.toEntity(mentorProfileDTO.getMentorDTO()));

        return mentorProfile;
    }

}
