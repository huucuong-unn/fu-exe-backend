package com.exe01.backend.converter;

import com.exe01.backend.dto.CompanyDTO;
import com.exe01.backend.entity.Company;
import com.exe01.backend.exception.BaseException;

public class CompanyConverter {

    public static CompanyDTO toDto(Company company) {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(company.getId());
        companyDTO.setAccount(AccountConverter.toDto(company.getAccount()));
        companyDTO.setName(company.getName());
        companyDTO.setCountry(company.getCountry());
        companyDTO.setAddress(company.getAddress());
        companyDTO.setAvatarUrl(company.getAvatarUrl());
        companyDTO.setCompanyWebsiteUrl(company.getCompany_website_url());
        companyDTO.setFacebookUrl(company.getFacebook_url());
        companyDTO.setDescription(company.getDescription());
        companyDTO.setWorkingTime(company.getWorkingTime());
        companyDTO.setCompanySize(company.getCompanySize());
        companyDTO.setCompanyType(company.getCompanyType());
        companyDTO.setAvatarUrl(company.getAvatarUrl());
        companyDTO.setStatus(company.getStatus());
        companyDTO.setCreatedDate(company.getCreatedDate());
        companyDTO.setModifiedDate(company.getModifiedDate());
        companyDTO.setCreatedBy(company.getCreatedBy());
        companyDTO.setModifiedBy(company.getModifiedBy());
        return companyDTO;
    }

    public static Company toEntity(CompanyDTO companyDTO) throws BaseException {
        Company company = new Company();
        company.setId(companyDTO.getId());
        company.setAccount(AccountConverter.toEntity(companyDTO.getAccount()));
        company.setName(companyDTO.getName());
        company.setCountry(companyDTO.getCountry());
        company.setAddress(companyDTO.getAddress());
        company.setAvatarUrl(companyDTO.getAvatarUrl());
        company.setCompany_website_url(companyDTO.getCompanyWebsiteUrl());
        company.setFacebook_url(companyDTO.getFacebookUrl());
        company.setDescription(companyDTO.getDescription());
        company.setWorkingTime(companyDTO.getWorkingTime());
        company.setCompanySize(companyDTO.getCompanySize());
        company.setCompanyType(companyDTO.getCompanyType());
        company.setAvatarUrl(companyDTO.getAvatarUrl());
        company.setStatus(companyDTO.getStatus());
        company.setCreatedDate(companyDTO.getCreatedDate());
        company.setModifiedDate(companyDTO.getModifiedDate());
        company.setCreatedBy(companyDTO.getCreatedBy());
        company.setModifiedBy(companyDTO.getModifiedBy());
        return company;
    }

}
