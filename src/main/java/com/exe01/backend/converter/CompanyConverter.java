package com.exe01.backend.converter;

import com.exe01.backend.dto.CompanyDTO;
import com.exe01.backend.entity.Company;

public class CompanyConverter {

    public static CompanyDTO toDto(Company company) {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(company.getId());
        companyDTO.setName(company.getName());
        companyDTO.setCountry(company.getCountry());
        companyDTO.setAddress(company.getAddress());
        companyDTO.setAvatarUrl(company.getAvatarUrl());
        companyDTO.setWorkingTime(company.getWorkingTime());
        companyDTO.setCompanySize(company.getCompanySize());
        companyDTO.setCompanyType(company.getCompanyType());
        companyDTO.setOvertimePolicy(company.getOvertimePolicy());
        companyDTO.setStatus(company.getStatus());
        companyDTO.setCreatedDate(company.getCreatedDate());
        companyDTO.setModifiedDate(company.getModifiedDate());
        companyDTO.setCreatedBy(company.getCreatedBy());
        companyDTO.setModifiedBy(company.getModifiedBy());
        return companyDTO;
    }

    public static Company toEntity(CompanyDTO companyDTO) {
        Company company = new Company();
        company.setId(companyDTO.getId());
        company.setName(companyDTO.getName());
        company.setCountry(companyDTO.getCountry());
        company.setAddress(companyDTO.getAddress());
        company.setAvatarUrl(companyDTO.getAvatarUrl());
        company.setWorkingTime(companyDTO.getWorkingTime());
        company.setCompanySize(companyDTO.getCompanySize());
        company.setCompanyType(companyDTO.getCompanyType());
        company.setOvertimePolicy(companyDTO.getOvertimePolicy());
        company.setStatus(companyDTO.getStatus());
        company.setCreatedDate(companyDTO.getCreatedDate());
        company.setModifiedDate(companyDTO.getModifiedDate());
        company.setCreatedBy(companyDTO.getCreatedBy());
        company.setModifiedBy(companyDTO.getModifiedBy());
        return company;
    }

}
