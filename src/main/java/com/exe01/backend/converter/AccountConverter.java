package com.exe01.backend.converter;

import com.exe01.backend.dto.AccountDTO;
import com.exe01.backend.entity.Account;

public class AccountConverter {
    public static AccountDTO toDto(Account account) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setPassword(account.getPassword());
        accountDTO.setId(account.getId());
        accountDTO.setUsername(account.getUsername());
        accountDTO.setAvatarUrl(account.getAvatarUrl());
        accountDTO.setCreatedDate(account.getCreatedDate());
        accountDTO.setModifiedDate(account.getModifiedDate());
        accountDTO.setCreateBy(account.getCreatedBy());
        accountDTO.setModifiedBy(account.getModifiedBy());
        accountDTO.setStatus(account.getStatus());
        accountDTO.setEmail(account.getEmail());
        accountDTO.setRole(account.getRole().getName());
        accountDTO.setPoint(account.getPoint());

        return accountDTO;
    }
}
