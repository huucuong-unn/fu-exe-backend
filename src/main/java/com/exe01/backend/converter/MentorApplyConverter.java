package com.exe01.backend.converter;

import com.exe01.backend.dto.MentorApplyDTO;
import com.exe01.backend.entity.MentorApply;
import com.exe01.backend.exception.BaseException;

public class MentorApplyConverter {

    public static MentorApplyDTO toDto(MentorApply mentorApply) {
        if (mentorApply == null) {
            return null;
        }

        MentorApplyDTO mentorApplyDTO = new MentorApplyDTO();
        mentorApplyDTO.setFeedback(mentorApply.getFeedback());
        mentorApplyDTO.setCampaign(CampaignConverter.toDto(mentorApply.getCampaign()));
        mentorApplyDTO.setStatus(mentorApply.getStatus());
        mentorApplyDTO.setApplication(ApplicationConverter.toDto(mentorApply.getApplication()));
        mentorApplyDTO.setMentee(MenteeConverter.toDto(mentorApply.getMentee()));

        return mentorApplyDTO;
    }

    public static MentorApply toEntity(MentorApplyDTO mentorApplyDTO) throws BaseException {
        if (mentorApplyDTO == null) {
            return null;
        }

        MentorApply mentorApply = new MentorApply();
        mentorApply.setFeedback(mentorApplyDTO.getFeedback());
        mentorApply.setCampaign(CampaignConverter.toEntity(mentorApplyDTO.getCampaign()));
        mentorApply.setStatus(mentorApplyDTO.getStatus());
        mentorApply.setApplication(ApplicationConverter.toEntity(mentorApplyDTO.getApplication()));
        mentorApply.setMentee(MenteeConverter.toEntity(mentorApplyDTO.getMentee()));

        return mentorApply;
    }
}
