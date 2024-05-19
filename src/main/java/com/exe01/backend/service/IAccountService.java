package com.exe01.backend.service;

import com.exe01.backend.dto.AccountDTO;
import com.exe01.backend.dto.request.account.CreateAccountRequest;
import com.exe01.backend.dto.request.account.UpdateAccountRequest;

import java.util.UUID;

public interface IAccountService extends IGenericService<AccountDTO> {
    
    public AccountDTO create(CreateAccountRequest request);

    public Boolean update(UUID id, UpdateAccountRequest request);

    public Boolean delete(UUID id);
}
