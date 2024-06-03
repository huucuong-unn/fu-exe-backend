package com.exe01.backend.service;

import com.exe01.backend.dto.MajorDTO;
import com.exe01.backend.dto.request.major.CreateMajorRequest;
import com.exe01.backend.dto.request.major.UpdateMajorRequest;
import com.exe01.backend.exception.BaseException;

import java.util.UUID;

public interface IMajorService extends IGenericService<MajorDTO>{

    MajorDTO create(CreateMajorRequest request) throws BaseException;

    Boolean update(UUID id, UpdateMajorRequest request) throws BaseException;

    Boolean delete(UUID id) throws BaseException;

    Boolean changeStatus(UUID id) throws BaseException;

}
