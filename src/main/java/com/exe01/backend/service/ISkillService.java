package com.exe01.backend.service;

import com.exe01.backend.dto.SkillDTO;
import com.exe01.backend.dto.request.skill.CreateSkillRequest;
import com.exe01.backend.dto.request.skill.UpdateSkillRequest;
import com.exe01.backend.exception.BaseException;

import java.util.UUID;

public interface ISkillService extends IGenericService<SkillDTO> {

    SkillDTO create(CreateSkillRequest request) throws BaseException;

    Boolean update(UUID id, UpdateSkillRequest request) throws BaseException;

    Boolean delete(UUID id) throws BaseException;

    Boolean changeStatus(UUID id) throws BaseException;

}
