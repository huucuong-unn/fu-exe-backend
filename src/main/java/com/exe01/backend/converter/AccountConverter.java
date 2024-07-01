package com.exe01.backend.converter;

import com.exe01.backend.dto.AccountDTO;
import com.exe01.backend.dto.CompanyDTO;
import com.exe01.backend.dto.MentorDTO;
import com.exe01.backend.dto.StudentDTO;
import com.exe01.backend.entity.*;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.repository.CompanyRepository;
import com.exe01.backend.repository.MentorRepository;
import com.exe01.backend.repository.StudentRepository;
import com.exe01.backend.service.ICompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

public class AccountConverter {

    public static AccountDTO toDto(Account account) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(account.getId());
        accountDTO.setUsername(account.getUsername());
        accountDTO.setAvatarUrl(account.getAvatarUrl());
        accountDTO.setCreatedDate(account.getCreatedDate());
        accountDTO.setModifiedDate(account.getModifiedDate());
        accountDTO.setCreateBy(account.getCreatedBy());
        accountDTO.setModifiedBy(account.getModifiedBy());
        accountDTO.setStatus(account.getStatus());
        accountDTO.setEmail(account.getEmail());
        accountDTO.setRole(RoleConverter.toDto(account.getRole()));
        accountDTO.setPoint(account.getPoint());
        return accountDTO;
    }

    public static Account toEntity(AccountDTO accountDTO) throws BaseException {
        Account account = new Account();
        account.setId(accountDTO.getId());
        account.setUsername(accountDTO.getUsername());
        account.setAvatarUrl(accountDTO.getAvatarUrl());
        account.setCreatedDate(accountDTO.getCreatedDate());
        account.setModifiedDate(accountDTO.getModifiedDate());
        account.setCreatedBy(accountDTO.getCreateBy());
        account.setModifiedBy(accountDTO.getModifiedBy());
        account.setStatus(accountDTO.getStatus());
        account.setEmail(accountDTO.getEmail());
        Role role = RoleConverter.toEntity(accountDTO.getRole());
        account.setRole(role);
        account.setPoint(accountDTO.getPoint());

        return account;
    }

}
