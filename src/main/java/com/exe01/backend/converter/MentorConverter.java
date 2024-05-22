package com.exe01.backend.converter;

import com.exe01.backend.dto.AccountDTO;
import com.exe01.backend.dto.MentorDTO;
import com.exe01.backend.dto.MentorProfileDTO;
import com.exe01.backend.entity.Mentor;

import java.util.List;

public class MentorConverter {

    public static MentorDTO toDto(Mentor mentor) {
        MentorDTO mentorDTO = new MentorDTO();
        mentorDTO.setId(mentor.getId());
        AccountDTO accountDTO = AccountConverter.toDto(mentor.getAccount());
        mentorDTO.setAccount(accountDTO);
        List<MentorProfileDTO> mentorProfileDTOs = mentor.getMentorProfiles().stream().map(MentorProfileConverter::toDto).toList();
        mentorDTO.setMentorProfiles(mentorProfileDTOs);
        mentorDTO.setStatus(mentor.getStatus());
        mentorDTO.setCreatedDate(mentor.getCreatedDate());
        mentorDTO.setModifiedDate(mentor.getModifiedDate());
        mentorDTO.setCreatedBy(mentor.getCreatedBy());
        mentorDTO.setModifiedBy(mentor.getModifiedBy());

        return mentorDTO;
    }

}
