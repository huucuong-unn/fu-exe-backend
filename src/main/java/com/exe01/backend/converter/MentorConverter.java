package com.exe01.backend.converter;

import com.exe01.backend.dto.AccountDTO;
import com.exe01.backend.dto.MentorDTO;
import com.exe01.backend.dto.response.mentorProfile.CreateMentorResponse;
import com.exe01.backend.entity.Account;
import com.exe01.backend.entity.Mentor;
import com.exe01.backend.exception.BaseException;

public class MentorConverter {

    public static MentorDTO toDto(Mentor mentor) {
        MentorDTO mentorDTO = new MentorDTO();
        mentorDTO.setId(mentor.getId());
        mentorDTO.setFullName(mentor.getFullName());
        AccountDTO accountDTO = AccountConverter.toDto(mentor.getAccount());
        mentorDTO.setAccount(accountDTO);
        mentorDTO.setCompany(CompanyConverter.toDto(mentor.getCompany()));
        mentorDTO.setStatus(mentor.getStatus());
        mentorDTO.setCreatedDate(mentor.getCreatedDate());
        mentorDTO.setModifiedDate(mentor.getModifiedDate());
        mentorDTO.setCreatedBy(mentor.getCreatedBy());
        mentorDTO.setModifiedBy(mentor.getModifiedBy());

        return mentorDTO;
    }

    public static Mentor toEntity(MentorDTO mentorDTO) throws BaseException {
        Mentor mentor = new Mentor();
        mentor.setId(mentorDTO.getId());
        mentor.setFullName(mentorDTO.getFullName());
        Account account = AccountConverter.toEntity(mentorDTO.getAccount());
        mentor.setAccount(account);
        mentor.setCompany(CompanyConverter.toEntity(mentorDTO.getCompany()));
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
        mentorDTO.setFullName(mentor.getFullName());
        mentorDTO.setStatus(mentor.getStatus());
        mentorDTO.setCreatedDate(mentor.getCreatedDate());
        mentorDTO.setModifiedDate(mentor.getModifiedDate());
        mentorDTO.setCreatedBy(mentor.getCreatedBy());
        mentorDTO.setModifiedBy(mentor.getModifiedBy());

        return mentorDTO;
    }

}
