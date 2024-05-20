package com.exe01.backend.service.impl;

import com.exe01.backend.constant.ConstStatus;
import com.exe01.backend.converter.AccountConverter;
import com.exe01.backend.dto.AccountDTO;
import com.exe01.backend.dto.request.account.CreateAccountRequest;
import com.exe01.backend.dto.request.account.UpdateAccountRequest;
import com.exe01.backend.entity.Account;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.repository.AccountRepository;
import com.exe01.backend.repository.RoleRepository;
import com.exe01.backend.service.IAccountService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccountServiceImpl implements IAccountService {

    Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public AccountDTO create(CreateAccountRequest request) {
        logger.info("Create major");
        Account account = new Account();
        account.setUsername(request.getUsername());
        account.setPassword(request.getPassword());
        account.setAvatarUrl(request.getAvatarUrl());
        account.setStatus(ConstStatus.ACTIVE_STATUS);
        account.setEmail(request.getEmail());

        var roleById = roleRepository.findById(request.getRoleId());
        boolean isRoleExist = roleById.isPresent() && roleById.get().getStatus().equals(ConstStatus.ACTIVE_STATUS);

        if (!isRoleExist) {
            logger.warn("Role with id {} is not found", request.getRoleId());
            //TODO
            throw new EntityNotFoundException();
        }

        account.setRole(roleById.get());

        accountRepository.save(account);

        return AccountConverter.toDto(account);
    }

    @Override
    public Boolean update(UUID id, UpdateAccountRequest request) {
        logger.info("Update major");
        var accountById = accountRepository.findById(id);
        boolean isAccountExist = accountById.isPresent();

        if (!isAccountExist) {
            logger.warn("Account with id {} is not found", id);
            //TODO
            throw new EntityNotFoundException();
        }

        Account account = new Account();
        account.setId(id);
        account.setUsername(request.getUsername());
        account.setPassword(request.getPassword());
        account.setAvatarUrl(request.getAvatarUrl());
        account.setStatus(request.getStatus());
        account.setEmail(request.getEmail());

        var roleById = roleRepository.findById(request.getRoleId());
        boolean isRoleExist = roleById.isPresent();

        if (!isRoleExist) {
            logger.warn("Role with id {} is not found", request.getRoleId());
            //TODO
            throw new EntityNotFoundException();
        }

        account.setRole(roleById.get());

        try {
            accountRepository.save(account);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Failed to save account", ex);
        } catch (Exception ex) {
            throw new RuntimeException("An unexpected error occurred", ex);
        }

        return true;
    }

    @Override
    public Boolean delete(UUID id) {
        logger.info("Delete account");
        var accountById = accountRepository.findById(id);
        boolean isAccountExist = accountById.isPresent();

        if (!isAccountExist) {
            logger.warn("Account with id {} is not found", id);
            //TODO
            throw new EntityNotFoundException();
        }

        accountById.get().setStatus(ConstStatus.INACTIVE_STATUS);

        try {
            accountRepository.save(accountById.get());
        } catch (DataAccessException ex) {
            throw new RuntimeException("Failed to save account", ex);
        } catch (Exception ex) {
            throw new RuntimeException("An unexpected error occurred", ex);
        }

        return true;
    }

    @Override
    public AccountDTO findById(UUID id) {
        logger.info("Find account by id {}", id);
        var accountById = accountRepository.findById(id);
        boolean isAccountExist = accountById.isPresent();

        if (!isAccountExist) {
            //TODO
            throw new EntityNotFoundException();
        }

        return AccountConverter.toDto(accountById.get());
    }

    @Override
    public PagingModel getAll(Integer page, Integer limit) {
        logger.info("Get all account with paging");
        PagingModel result = new PagingModel();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);

        List<Account> accounts = accountRepository.findAllByOrderByCreatedDate(pageable);

        List<AccountDTO> accountDTOs = accounts.stream().map(AccountConverter::toDto).toList();

        result.setListResult(accountDTOs);

        result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
        result.setLimit(limit);

        return result;
    }

    public int totalItem() {
        return (int) accountRepository.count();
    }

    @Override
    public PagingModel findAllByStatusTrue(Integer page, Integer limit) {
        logger.info("Get all account with status is active");
        PagingModel result = new PagingModel();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);

        List<Account> accounts = accountRepository.findAllByStatusOrderByCreatedDate(ConstStatus.ACTIVE_STATUS, pageable);

        List<AccountDTO> accountDTOs = accounts.stream().map(AccountConverter::toDto).toList();

        result.setListResult(accountDTOs);

        result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
        result.setLimit(limit);

        return result;
    }
}
