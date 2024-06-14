package com.exe01.backend.service;

import com.exe01.backend.dto.UniversityDTO;
import com.exe01.backend.dto.request.university.CreateUniversityRequest;
import com.exe01.backend.dto.request.university.UpdateUniversityRequest;
import com.exe01.backend.dto.response.university.UniversityDropDownListResponse;
import com.exe01.backend.entity.University;
import com.exe01.backend.exception.BaseException;

import java.util.List;
import java.util.UUID;

public interface IUniversityService extends IGenericService<UniversityDTO> {

    Boolean update(UUID id, UpdateUniversityRequest request) throws BaseException;

    UniversityDTO create(CreateUniversityRequest request) throws BaseException;

    Boolean delete(UUID id) throws BaseException;

    Boolean changeStatus(UUID id) throws BaseException;

    List<UniversityDropDownListResponse> getAll() throws BaseException;
}
