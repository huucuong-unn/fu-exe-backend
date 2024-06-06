package com.exe01.backend.service.impl;

import com.exe01.backend.constant.ConstError;
import com.exe01.backend.constant.ConstHashKeyPrefix;
import com.exe01.backend.constant.ConstStatus;
import com.exe01.backend.converter.AccountConverter;
import com.exe01.backend.converter.RoleConverter;
import com.exe01.backend.dto.AccountDTO;
import com.exe01.backend.dto.request.account.CreateAccountRequest;
import com.exe01.backend.dto.request.account.LoginRequest;
import com.exe01.backend.dto.request.account.UpdateAccountRequest;
import com.exe01.backend.dto.response.JwtAuthenticationResponse;
import com.exe01.backend.entity.Account;
import com.exe01.backend.entity.Role;
import com.exe01.backend.enums.ErrorCode;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.repository.AccountRepository;
import com.exe01.backend.service.IAccountService;
import com.exe01.backend.service.IRoleService;
import com.exe01.backend.validation.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AccountServiceImpl implements IAccountService {

    Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private IRoleService roleService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTServiceImpl jwtService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public JwtAuthenticationResponse create(CreateAccountRequest request) throws BaseException {
        try {
            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
            logger.info("Create account");
            Account account = new Account();
            account.setUsername(request.getUsername());
            account.setPassword(passwordEncoder.encode(request.getPassword()));
            account.setAvatarUrl(request.getAvatarUrl());
            account.setStatus(ConstStatus.ACTIVE_STATUS);
            account.setEmail(request.getEmail());
            account.setPoint(0);

            Role role = RoleConverter.toEntity(roleService.findById(request.getRoleId()));

            account.setRole(role);

            accountRepository.save(account);
            var jwtToken = jwtService.generateToken(account);


            Set<String> keysToDelete = redisTemplate.keys("Account:*");
            if (ValidateUtil.IsNotNullOrEmptyForSet(keysToDelete)) {
                redisTemplate.delete(keysToDelete);
            }

       return MappingjwtAuthenticationRespone(account);
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

            Account accountById = AccountConverter.toEntity(findById(id));

            accountById.setId(id);
            accountById.setUsername(request.getUsername());
            accountById.setPassword(request.getPassword());
            accountById.setAvatarUrl(request.getAvatarUrl());
            accountById.setStatus(request.getStatus());
            accountById.setEmail(request.getEmail());

            Role roleById = RoleConverter.toEntity(roleService.findById(request.getRoleId()));

            accountById.setRole(roleById);

            accountRepository.save(accountById);

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
            Account accountById = AccountConverter.toEntity(findById(id));

            accountById.setId(id);
            accountById.setStatus(ConstStatus.INACTIVE_STATUS);

            accountRepository.save(accountById);

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

    private int totalItem() {
        return (int) accountRepository.count();
    }

    private int totalItemWithStatusActive() {
        return accountRepository.countByStatus(ConstStatus.ACTIVE_STATUS);
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
            Account accountById = AccountConverter.toEntity(findById(id));

            if (accountById.getStatus().equals(ConstStatus.ACTIVE_STATUS)) {
                accountById.setStatus(ConstStatus.INACTIVE_STATUS);
            } else {
                accountById.setStatus(ConstStatus.ACTIVE_STATUS);
            }

            accountRepository.save(accountById);

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
    public JwtAuthenticationResponse login(LoginRequest loginRequest) throws BaseException {
        // * method authenticate() của AuthenticationManager dùng để tạo ra một object Authentication object
        // ? Với UsernamePasswordAuthenticationToken là class implements từ Authentication, đại diện cho 1 authentication object
        // todo Trả về một object Authentication và đưa vào Security Context để quản lý
        Account account = findByUsername(loginRequest.getUsername());

        // Check if the account is disabled
        if (account.getStatus().equals(ConstStatus.INACTIVE_STATUS)) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), "User is disabled", ErrorCode.ERROR_500.getMessage());
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));

       // Account account = findByUsername(loginRequest.getUsername());
return MappingjwtAuthenticationRespone(account);
    }

    private JwtAuthenticationResponse MappingjwtAuthenticationRespone(Account account)
    {
        JwtAuthenticationResponse jwtAuthenticationRespone = new JwtAuthenticationResponse();
        jwtAuthenticationRespone.setId(account.getId());
        jwtAuthenticationRespone.setUsername(account.getUsername());
        jwtAuthenticationRespone.setAvatarUrl(account.getAvatarUrl());
        jwtAuthenticationRespone.setStatus(account.getStatus());
        jwtAuthenticationRespone.setEmail(account.getEmail());
        jwtAuthenticationRespone.setRole(account.getRole().getName());
        jwtAuthenticationRespone.setPoint(account.getPoint());

        var jwt = jwtService.generateToken(account);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), account);

        jwtAuthenticationRespone.setToken(jwt);
        jwtAuthenticationRespone.setRefreshToken(refreshToken);
        return jwtAuthenticationRespone;
    }

    @Override
    public Account findByUsername(String username) throws BaseException {
        try {
            logger.info("Find account by username {}", username);
            String hashKeyForAccount = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ACCOUNT + username;
            Account accountDTOByRedis = (Account) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ACCOUNT, hashKeyForAccount);

            if (!Objects.isNull(accountDTOByRedis)) {
                return accountDTOByRedis;
            }

            Optional<Account> accountById = accountRepository.findByUsername(username);
            boolean isAccountExist = accountById.isPresent();

            if (!isAccountExist) {
                logger.warn("Account with id {} is not found", username);
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Account.ACCOUNT_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }


            redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ACCOUNT, hashKeyForAccount, accountById.get());

            return accountById.get();
        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }
}
