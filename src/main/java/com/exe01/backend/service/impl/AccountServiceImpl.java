package com.exe01.backend.service.impl;

import com.exe01.backend.bucket.BucketName;
import com.exe01.backend.constant.ConstError;
import com.exe01.backend.constant.ConstHashKeyPrefix;
import com.exe01.backend.constant.ConstStatus;
import com.exe01.backend.converter.AccountConverter;
import com.exe01.backend.converter.MenteeConverter;
import com.exe01.backend.converter.RoleConverter;
import com.exe01.backend.converter.StudentConverter;
import com.exe01.backend.dto.AccountDTO;
import com.exe01.backend.dto.MenteeDTO;
import com.exe01.backend.dto.StudentDTO;
import com.exe01.backend.dto.request.account.CreateAccountRequest;
import com.exe01.backend.dto.request.account.LoginRequest;
import com.exe01.backend.dto.request.account.UpdateAccountRequest;
import com.exe01.backend.dto.response.JwtAuthenticationResponse;
import com.exe01.backend.entity.*;
import com.exe01.backend.enums.ErrorCode;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.fileStore.FileStore;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.repository.*;
import com.exe01.backend.service.IAccountService;
import com.exe01.backend.service.IMenteeService;
import com.exe01.backend.service.IRoleService;
import com.exe01.backend.service.IStudentService;
import com.exe01.backend.validation.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
import org.springframework.web.multipart.MultipartFile;

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
    @Lazy
    IStudentService studentService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    FileStore fileStore;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public JwtAuthenticationResponse create(CreateAccountRequest request) throws BaseException {
        try {
            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
            logger.info("Create account");
            // check dupplicate usernam
            if (accountRepository.findByUsername(request.getUsername()).isPresent()) {
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Account.USERNAME_EXISTED, ErrorCode.ERROR_500.getMessage());
            }
            // check dupplicate email
            if (accountRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Account.EMAIL_EXISTED, ErrorCode.ERROR_500.getMessage());
            }

            Account account = new Account();
            account.setUsername(request.getUsername());
            account.setPassword(passwordEncoder.encode(request.getPassword()));
            account.setStatus(ConstStatus.ACTIVE_STATUS);
            account.setEmail(request.getEmail());
            account.setPoint(0);

            Role role = RoleConverter.toEntity(roleService.findByName(request.getRoleName()));

            account.setRole(role);

            accountRepository.save(account);

            switch (account.getRole().getName()) {
                case "admin":
                    break;
                case "company":

                    break;
                case "mentor":
                    System.out.println("It's a cherry!");
                    break;
                case "mentee":
                    System.out.println("It's a cherry!");
                    break;
                default:
                    break;
            }
            uploadAccountImage(account.getId(), request.getAvatarUrl());
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
            // accountById.setAvatarUrl(request.getAvatarUrl());
            accountById.setStatus(request.getStatus());
            accountById.setEmail(request.getEmail());

            Role roleById = RoleConverter.toEntity(roleService.findByName(request.getRoleName()));

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
Account account = accountRepository.findById(id).orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND.value(), "Account not found", HttpStatus.NOT_FOUND.getReasonPhrase()));

            if (account.getStatus().equals(ConstStatus.ACTIVE_STATUS)) {
                account.setStatus(ConstStatus.INACTIVE_STATUS);
            } else {
                account.setStatus(ConstStatus.ACTIVE_STATUS);
            }

            Set<String> keysToDelete = redisTemplate.keys("Account:*");
            if (ValidateUtil.IsNotNullOrEmptyForSet(keysToDelete)) {
                redisTemplate.delete(keysToDelete);
            }
            accountRepository.save(account);
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
        Account account = findByEmailOrUserName(loginRequest.getEmailOrUsername());

        // Check if the account is disabled
        if (account.getStatus().equals(ConstStatus.INACTIVE_STATUS)) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), "User is disabled", ErrorCode.ERROR_500.getMessage());
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(account.getUsername(),
                loginRequest.getPassword()));

        // Account account = findByUsername(loginRequest.getUsername());
        return MappingjwtAuthenticationRespone(account);
    }

    private JwtAuthenticationResponse MappingjwtAuthenticationRespone(Account account) throws BaseException {
        JwtAuthenticationResponse jwtAuthenticationRespone = new JwtAuthenticationResponse();
        Optional<Student> student = studentRepository.findByAccountId(account.getId()) ;
        Optional<Mentor> mentor = mentorRepository.findByAccountId(account.getId());
        Optional<Company> company = companyRepository.findByAccountId(account.getId());
        jwtAuthenticationRespone.setId(account.getId());

        if(student.isPresent()){
            jwtAuthenticationRespone.setStudentId(student.get().getId());
        }
        if(mentor.isPresent()){
            jwtAuthenticationRespone.setMentorId(mentor.get().getId());
        }
        if(company.isPresent()){
            jwtAuthenticationRespone.setCompanyId(company.get().getId());
        }

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


    private Account findByEmailOrUserName(String emailOrUserName) throws BaseException {
        try {
            logger.info("Find account by email or username {}", emailOrUserName);
            String hashKeyForAccount = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ACCOUNT + emailOrUserName;
            Account accountDTOByRedis = (Account) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ACCOUNT, hashKeyForAccount);

            if (!Objects.isNull(accountDTOByRedis)) {
                return accountDTOByRedis;
            }

            Optional<Account> accountById;
            accountById = accountRepository.findByEmail(emailOrUserName);
            if (!accountById.isPresent()) {
                accountById = accountRepository.findByUsername(emailOrUserName);
            }

            boolean isAccountExist = accountById.isPresent();

            if (!isAccountExist) {
                logger.warn("Account with email or username {} is not found", emailOrUserName);
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

//    @Override
//    public void uploadAccountImage(UUID accountId, MultipartFile file) throws BaseException{
//
//        try {
//
//
//        // 1. Check if image is not empty
//        if(file.isEmpty()){
//            throw new IllegalStateException("Cannot upload empty file [ " + file.getSize() + "]");
//        }
//        // 2. If file is an image
//        if(!Arrays.asList("image/jpeg", "image/png", "image/jpg").contains(file.getContentType())){
//            throw new IllegalStateException("File must be an image [ " + file.getContentType() + "]");
//        }
//        // 3. The user is exist in our database
//Account account = accountRepository.findById(accountId).get();
//            // 4. Grab some metadata from file if any
//            Map<String, String> metadata = new HashMap<>();
//            metadata.put("Content-Type", file.getContentType());
//            metadata.put("Content-Length", String.valueOf(file.getSize()));
//
//        // 5. Store the image in s3 and update database (userProfileImageLink) with s3 image link
//           String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), account.getId());
//         String filename =   String.format( "%s-%s", file.getOriginalFilename(), UUID.randomUUID());
//            fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
//            account.setAvatarUrl(filename);
//          accountRepository.save(account);
//        }catch (Exception baseException) {
//            if (baseException instanceof BaseException) {
//            }
//            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
//        }
//    }

    @Override
    public void uploadAccountImage(UUID accountId, MultipartFile file) throws BaseException {
        try {
            // 1. Check if image is not empty
            if (file.isEmpty()) {
                throw new IllegalStateException("Cannot upload empty file [ " + file.getSize() + "]");
            }
            // 2. If file is an image
            if (!Arrays.asList("image/jpeg", "image/png", "image/jpg").contains(file.getContentType())) {
                throw new IllegalStateException("File must be an image [ " + file.getContentType() + "]");
            }
            // 3. The user exists in our database
            Account account = accountRepository.findById(accountId).orElseThrow(() -> new IllegalStateException("Account not found"));
            // 4. Grab some metadata from file if any
            Map<String, String> metadata = new HashMap<>();
            metadata.put("Content-Type", file.getContentType());
            metadata.put("Content-Length", String.valueOf(file.getSize()));

            // 5. Store the image in S3 and update database (userProfileImageLink) with S3 image link
            String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), account.getId());
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            String filename = String.format("%s-%s%s", UUID.randomUUID(), originalFilename.substring(0, originalFilename.lastIndexOf('.')), extension);
            fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
            account.setAvatarUrl(filename);
            accountRepository.save(account);
        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw (BaseException) baseException;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public byte[] downloadAccountImage(UUID accountId) throws BaseException {
        Account account = AccountConverter.toEntity(findById(accountId));
        String path = BucketName.PROFILE_IMAGE.getBucketName();

        var file = fileStore.download(path, account.getAvatarUrl());
        return file;
    }

    @Override
    public Boolean updatePoint(UUID id, Integer point) throws BaseException {
        try {
            logger.info("Update point for account");
            Account accountById = accountRepository.findById(id).orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND.value(), "Account not found", HttpStatus.NOT_FOUND.getReasonPhrase()));

            accountById.setPoint(accountById.getPoint() + point);

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
    public Map<String, Object> getAccountMenteeInfo(UUID accountId) {
        StudentDTO student = null;
        MenteeDTO mentee = null;
        Map<String, Object> accountInfo = new HashMap<>();
        try {
            student = StudentConverter.toDto(studentRepository.findByAccountId(accountId).get());
            accountInfo.put("mentee", student);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return accountInfo;
    }

    @Override
    public PagingModel findAllForAdmin(String userName, String email, String role, String status, int page, int limit) throws BaseException {

        try {
            logger.info("Get all account with paging");
            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            String hashKeyForAccount = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ACCOUNT + "all:" + userName + email + role + status + page + ":" + limit;

            List<AccountDTO> accountDTOs;

            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ACCOUNT, hashKeyForAccount)) {
                logger.info("Fetching account from cache for page {} and limit {}", page, limit);
                accountDTOs = (List<AccountDTO>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ACCOUNT, hashKeyForAccount);
            } else {
                logger.info("Fetching account from database for page {} and limit {}", page, limit);
                List<Account> accounts = accountRepository.findAllForAdmin(userName, email, role, status, pageable);
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
}

