package com.exe01.backend.service.impl;

import com.exe01.backend.constant.ConstError;
import com.exe01.backend.constant.ConstHashKeyPrefix;
import com.exe01.backend.constant.ConstStatus;
import com.exe01.backend.converter.AccountConverter;
import com.exe01.backend.dto.AccountDTO;
import com.exe01.backend.dto.request.account.CreateAccountRequest;
import com.exe01.backend.dto.request.account.UpdateAccountRequest;
import com.exe01.backend.entity.Account;
import com.exe01.backend.entity.Role;
import com.exe01.backend.enums.ErrorCode;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.repository.AccountRepository;
import com.exe01.backend.repository.RoleRepository;
import com.exe01.backend.service.IAccountService;
import com.exe01.backend.validation.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AccountServiceImpl implements IAccountService {

    Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public AccountDTO create(CreateAccountRequest request) throws BaseException {
        try {
            logger.info("Create major");
            Account account = new Account();
            account.setUsername(request.getUsername());
            account.setPassword(request.getPassword());
            account.setAvatarUrl(request.getAvatarUrl());
            account.setStatus(ConstStatus.ACTIVE_STATUS);
            account.setEmail(request.getEmail());
            account.setPoint(0);

            Optional<Role> roleById = roleRepository.findById(request.getRoleId());
            boolean isRoleExist = roleById.isPresent() && roleById.get().getStatus().equals(ConstStatus.ACTIVE_STATUS);

            if (!isRoleExist) {
                logger.warn("Role with id {} is not found", request.getRoleId());
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Role.ROLE_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            account.setRole(roleById.get());

            accountRepository.save(account);

            Set<String> keysToDelete = redisTemplate.keys("Account:*");
            if (ValidateUtil.IsNotNullOrEmptyForSet(keysToDelete)) {
                redisTemplate.delete(keysToDelete);
            }

            return AccountConverter.toDto(account);
        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }

    }

    @Override
    public Boolean update(UUID id, UpdateAccountRequest request) throws BaseException {
        try {
            logger.info("Update major");
            Optional<Account> accountById = accountRepository.findById(id);
            boolean isAccountExist = accountById.isPresent();

            if (!isAccountExist) {
                logger.warn("Account with id {} is not found", id);
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Account.ACCOUNT_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            Account account = new Account();
            account.setId(id);
            account.setUsername(request.getUsername());
            account.setPassword(request.getPassword());
            account.setAvatarUrl(request.getAvatarUrl());
            account.setStatus(request.getStatus());
            account.setEmail(request.getEmail());

            Optional<Role> roleById = roleRepository.findById(request.getRoleId());
            boolean isRoleExist = roleById.isPresent() && roleById.get().getStatus().equals(ConstStatus.ACTIVE_STATUS);

            if (!isRoleExist) {
                logger.warn("Role with id {} is not found", request.getRoleId());
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Role.ROLE_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            account.setRole(roleById.get());

            accountRepository.save(account);

            Set<String> keysToDelete = redisTemplate.keys("Account:*");
            if (ValidateUtil.IsNotNullOrEmptyForSet(keysToDelete)) {
                redisTemplate.delete(keysToDelete);
            }

            return true;
        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }

    }

    @Override
    public Boolean delete(UUID id) throws BaseException {
        try {
            logger.info("Delete account");
            var accountById = accountRepository.findById(id);
            boolean isAccountExist = accountById.isPresent();

            if (!isAccountExist) {
                logger.warn("Account with id {} is not found", id);
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Account.ACCOUNT_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            accountById.get().setStatus(ConstStatus.INACTIVE_STATUS);

            accountRepository.save(accountById.get());

            Set<String> keysToDelete = redisTemplate.keys("Account:*");
            if (ValidateUtil.IsNotNullOrEmptyForSet(keysToDelete)) {
                redisTemplate.delete(keysToDelete);
            }

            return true;
        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }

    }

    @Override
    public AccountDTO findById(UUID id) throws BaseException {
        try {
            logger.info("Find account by id {}", id);
            String hashKeyForAccount = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ACCOUNT + id.toString();
            AccountDTO accountDTOByRedis = (AccountDTO) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ACCOUNT, hashKeyForAccount);

            if (!Objects.isNull(accountDTOByRedis)) {
                return accountDTOByRedis;
            }

            Optional<Account> accountById = accountRepository.findById(id);
            boolean isAccountExist = accountById.isPresent();

            if (!isAccountExist) {
                logger.warn("Account with id {} is not found", id);
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Account.ACCOUNT_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            AccountDTO accountDTO = AccountConverter.toDto(accountById.get());
            redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ACCOUNT, hashKeyForAccount, accountDTO);

            return accountDTO;
        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public PagingModel getAll(Integer page, Integer limit) throws BaseException {
        logger.info("Get all account with paging");
        try {
            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            String hashKeyForAccount = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ACCOUNT + "all:" + page + ":" + limit;

            List<AccountDTO> accountDTOs;

            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ACCOUNT, hashKeyForAccount)) {
                logger.info("Fetching account from cache for page {} and limit {}", page, limit);
                accountDTOs = (List<AccountDTO>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ACCOUNT, hashKeyForAccount);
            } else {
                logger.info("Fetching account from database for page {} and limit {}", page, limit);
                List<Account> accounts = accountRepository.findAllByOrderByCreatedDate(pageable);
                accountDTOs = accounts.stream().map(AccountConverter::toDto).toList();
                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ACCOUNT, hashKeyForAccount, accountDTOs);
            }

            result.setListResult(accountDTOs);

            result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
            result.setLimit(limit);

            return result;
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    public int totalItem() {
        return (int) accountRepository.count();
    }

    public int totalItemWithStatusActive() {
        return (int) accountRepository.countByStatus(ConstStatus.ACTIVE_STATUS);
    }

    @Override
    public PagingModel findAllByStatusTrue(Integer page, Integer limit) throws BaseException {
        logger.info("Get all account with paging");
        try {
            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            String hashKeyForAccount = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ACCOUNT + "all:" + "active:" + page + ":" + limit;

            List<AccountDTO> accountDTOs;

            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ACCOUNT, hashKeyForAccount)) {
                logger.info("Fetching account from cache for page {} and limit {}", page, limit);
                accountDTOs = (List<AccountDTO>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ACCOUNT, hashKeyForAccount);
            } else {
                logger.info("Fetching account from database for page {} and limit {}", page, limit);
                List<Account> accounts = accountRepository.findAllByStatusOrderByCreatedDate(ConstStatus.ACTIVE_STATUS, pageable);
                accountDTOs = accounts.stream().map(AccountConverter::toDto).toList();
                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ACCOUNT, hashKeyForAccount, accountDTOs);
            }

            result.setListResult(accountDTOs);

            result.setTotalPage(((int) Math.ceil((double) (totalItemWithStatusActive()) / limit)));
            result.setLimit(limit);

            return result;
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    public Boolean changeStatus(UUID id) throws BaseException {
        try {
            logger.info("Find account by id {}", id);
            Optional<Account> accountById = accountRepository.findById(id);
            boolean isAccountExist = accountById.isPresent();

            if (!isAccountExist) {
                logger.warn("Account with id {} is not found", id);
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Account.ACCOUNT_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            if (accountById.get().getStatus().equals(ConstStatus.ACTIVE_STATUS)) {
                accountById.get().setStatus(ConstStatus.INACTIVE_STATUS);
            } else {
                accountById.get().setStatus(ConstStatus.ACTIVE_STATUS);
            }

            accountRepository.save(accountById.get());

            Set<String> keysToDelete = redisTemplate.keys("Account:*");
            if (ValidateUtil.IsNotNullOrEmptyForSet(keysToDelete)) {
                redisTemplate.delete(keysToDelete);
            }

            return true;
        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }
}
