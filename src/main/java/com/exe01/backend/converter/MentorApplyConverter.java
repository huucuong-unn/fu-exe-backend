package com.exe01.backend.converter;

import com.exe01.backend.dto.MentorApplyDTO;
import com.exe01.backend.entity.MentorApply;
import com.exe01.backend.dto.ApplicationDTO;

public class MentorApplyConverter {

    public static MentorApplyDTO toDto(MentorApply mentorApply) {
        if (mentorApply == null) {
            return null;
        }

        MentorApplyDTO mentorApplyDTO = new MentorApplyDTO();
        mentorApplyDTO.setFeedback(mentorApply.getFeedback());
        mentorApplyDTO.setApplication(ApplicationConverter.toDto(mentorApply.getApplication()));

        return mentorApplyDTO;
    }

    public static MentorApply toEntity(MentorApplyDTO mentorApplyDTO) {
        if (mentorApplyDTO == null) {
            return null;
        }

        MentorApply mentorApply = new MentorApply();
        mentorApply.setFeedback(mentorApplyDTO.getFeedback());
        mentorApply.setApplication(ApplicationConverter.toEntity(mentorApplyDTO.getApplication()));

        return mentorApply;
    }
}
