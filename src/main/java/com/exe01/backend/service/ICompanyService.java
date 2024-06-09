package com.exe01.backend.service;

import com.exe01.backend.dto.CompanyDTO;
import com.exe01.backend.dto.request.company.BaseCompanyRequest;
import com.exe01.backend.exception.BaseException;

import java.util.UUID;

public interface ICompanyService extends IGenericService<CompanyDTO>{

    CompanyDTO create(BaseCompanyRequest request) throws BaseException;

    Boolean update(UUID id, BaseCompanyRequest request) throws BaseException;

    Boolean delete(UUID id) throws BaseException;

    Boolean changeStatus(UUID id) throws BaseException;

}
