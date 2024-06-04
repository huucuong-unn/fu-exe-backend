package com.exe01.backend.converter;

import com.exe01.backend.dto.AccountDTO;
import com.exe01.backend.dto.MentorDTO;
import com.exe01.backend.dto.response.mentorProfile.CreateMentorResponse;
import com.exe01.backend.entity.Account;
import com.exe01.backend.entity.Mentor;
import com.exe01.backend.entity.MentorProfile;
import com.exe01.backend.validation.ValidateUtil;

import java.util.List;

public class MentorConverter {

    public static MentorDTO toDto(Mentor mentor) {
        MentorDTO mentorDTO = new MentorDTO();
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

    public static Mentor toEntity(MentorDTO mentorDTO) {
        Mentor mentor = new Mentor();
        mentor.setId(mentorDTO.getId());
        Account account = AccountConverter.toEntity(mentorDTO.getAccount());
        mentor.setAccount(account);
        mentor.setStatus(mentorDTO.getStatus());
        mentor.setCreatedDate(mentorDTO.getCreatedDate());
        mentor.setModifiedDate(mentorDTO.getModifiedDate());
        mentor.setCreatedBy(mentorDTO.getCreatedBy());
        mentor.setModifiedBy(mentorDTO.getModifiedBy());

        return mentor;
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
