package com.exe01.backend.converter;

import com.exe01.backend.dto.AccountDTO;
import com.exe01.backend.dto.MentorDTO;
import com.exe01.backend.dto.MentorProfileDTO;
import com.exe01.backend.dto.response.mentorProfile.CreateMentorResponse;
import com.exe01.backend.entity.Mentor;
import com.exe01.backend.entity.MentorProfile;

import java.util.List;

public class MentorConverter {

    public static MentorDTO toDto(Mentor mentor, List<MentorProfile> mentorProfiles) {
        MentorDTO mentorDTO = new MentorDTO();
        mentorDTO.setId(mentor.getId());
        AccountDTO accountDTO = AccountConverter.toDto(mentor.getAccount());
        mentorDTO.setAccount(accountDTO);
        mentorDTO.setMentorProfiles(mentorProfiles.stream().map(MentorProfileConverter::toDto).toList());
        mentorDTO.setStatus(mentor.getStatus());
        mentorDTO.setCreatedDate(mentor.getCreatedDate());
        mentorDTO.setModifiedDate(mentor.getModifiedDate());
        mentorDTO.setCreatedBy(mentor.getCreatedBy());
        mentorDTO.setModifiedBy(mentor.getModifiedBy());

        return mentorDTO;
    }

    public static CreateMentorResponse toCreateMentorResponse(Mentor mentor) {
        CreateMentorResponse mentorDTO = new CreateMentorResponse();
        mentorDTO.setId(mentor.getId());
        AccountDTO accountDTO = AccountConverter.toDto(mentor.getAccount());
        mentorDTO.setAccount(accountDTO);
        mentorDTO.setStatus(mentor.getStatus());
        mentorDTO.setCreatedDate(mentor.getCreatedDate());
        mentorDTO.setModifiedDate(mentor.getModifiedDate());
        mentorDTO.setCreatedBy(mentor.getCreatedBy());
        mentorDTO.setModifiedBy(mentor.getModifiedBy());

        return mentorDTO;
    }

}
