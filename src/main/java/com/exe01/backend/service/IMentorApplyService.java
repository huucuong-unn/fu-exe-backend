package com.exe01.backend.service;

import com.exe01.backend.dto.MentorApplyDTO;
import com.exe01.backend.dto.request.mentorApply.BaseMentorApplyRequest;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;

import java.util.UUID;

public interface IMentorApplyService {

    MentorApplyDTO findById(UUID id) throws BaseException;

    MentorApplyDTO create(BaseMentorApplyRequest request) throws BaseException;

    Boolean update(UUID id, BaseMentorApplyRequest request) throws BaseException;

    Boolean changeStatus(UUID id) throws BaseException;

    PagingModel findByApplicationMentorId(UUID mentorId, int page, int limit) throws BaseException;

    PagingModel findByMenteeId(UUID menteeId, int page, int limit) throws BaseException;

    PagingModel findAllByMenteeNameAndMentorFullNameAndCampaignIdAndCompanyId(String menteeName, String mentorFullName, UUID campaignId, UUID companyId ,int page, int limit) throws BaseException;
}