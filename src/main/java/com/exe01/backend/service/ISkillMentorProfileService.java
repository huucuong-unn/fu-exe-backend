package com.exe01.backend.service;

import com.exe01.backend.dto.SkillMentorProfileDTO;
import com.exe01.backend.dto.request.skillMentorProfile.BaseSkillMentorProfileRequest;
import com.exe01.backend.exception.BaseException;

public interface ISkillMentorProfileService extends IGenericService<SkillMentorProfileDTO>{

    SkillMentorProfileDTO create(BaseSkillMentorProfileRequest request) throws BaseException;

}
