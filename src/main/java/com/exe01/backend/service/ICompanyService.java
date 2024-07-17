package com.exe01.backend.service;

import com.exe01.backend.dto.CompanyDTO;
import com.exe01.backend.dto.Dashboard.TopFiveCompany;
import com.exe01.backend.dto.request.company.BaseCompanyRequest;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;

import java.util.List;
import java.util.UUID;

public interface ICompanyService extends IGenericService<CompanyDTO> {

    CompanyDTO create(BaseCompanyRequest request) throws BaseException;

    Boolean update(UUID id, BaseCompanyRequest request) throws BaseException;

    Boolean delete(UUID id) throws BaseException;

    Boolean changeStatus(UUID id) throws BaseException;

    PagingModel searchSortCompany(String name, String address, Integer page, Integer limit) throws BaseException;

    List<CompanyDTO> findAllByStatus() throws BaseException;

    List<TopFiveCompany> getTopFiveCompany() throws BaseException;


}
