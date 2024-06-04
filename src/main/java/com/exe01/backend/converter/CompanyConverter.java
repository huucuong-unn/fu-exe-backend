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
        companyDTO.setImg(company.getImg());
        companyDTO.setKeySkill(company.getKeySkill());
        companyDTO.setTopReason(company.getTopReason());
        companyDTO.setUrl(company.getUrl());
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

}
