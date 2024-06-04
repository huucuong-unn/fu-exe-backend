package com.exe01.backend.service;

import com.exe01.backend.dto.MenteeDTO;
import com.exe01.backend.dto.request.Mentee.MenteeRequest;
import com.exe01.backend.exception.BaseException;

import java.util.UUID;

public interface IMenteeService extends IGenericService<MenteeDTO>{

    MenteeDTO create(MenteeRequest menteeRequest) throws BaseException;

    Boolean changeStatus(UUID id) throws BaseException;



}
