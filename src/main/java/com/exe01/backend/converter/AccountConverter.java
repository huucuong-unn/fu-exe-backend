package com.exe01.backend.converter;

import com.exe01.backend.dto.AccountDTO;
import com.exe01.backend.entity.Account;
import com.exe01.backend.entity.Role;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountConverter {

    @Autowired
    private static IRoleService roleService;
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

    public static Account toEntity(AccountDTO accountDTO) throws BaseException {
        Account account = new Account();
        account.setId(accountDTO.getId());
        account.setPassword(accountDTO.getPassword());
        account.setUsername(accountDTO.getUsername());
        account.setAvatarUrl(accountDTO.getAvatarUrl());
        account.setCreatedDate(accountDTO.getCreatedDate());
        account.setModifiedDate(accountDTO.getModifiedDate());
        account.setCreatedBy(accountDTO.getCreateBy());
        account.setModifiedBy(accountDTO.getModifiedBy());
        account.setStatus(accountDTO.getStatus());
        account.setEmail(accountDTO.getEmail());
        Role role = RoleConverter.toEntity(roleService.findByName(accountDTO.getRole()));
        account.setRole(role);

        account.setPoint(accountDTO.getPoint());

        return account;
    }

}
