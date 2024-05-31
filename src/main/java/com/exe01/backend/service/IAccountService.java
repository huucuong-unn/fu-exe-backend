package com.exe01.backend.service;

import com.exe01.backend.dto.AccountDTO;
import com.exe01.backend.dto.request.account.CreateAccountRequest;
import com.exe01.backend.dto.request.account.UpdateAccountRequest;
import com.exe01.backend.exception.BaseException;

import java.util.UUID;

public interface IAccountService extends IGenericService<AccountDTO> {
    
    AccountDTO create(CreateAccountRequest request) throws BaseException;

    Boolean update(UUID id, UpdateAccountRequest request) throws BaseException;

    Boolean delete(UUID id) throws BaseException;

    Boolean changeStatus(UUID id) throws BaseException;
}
