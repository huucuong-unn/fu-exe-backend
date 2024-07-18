package com.exe01.backend.service;

import com.exe01.backend.dto.AccountDTO;
import com.exe01.backend.dto.request.SignUpWithStudentRequest;
import com.exe01.backend.dto.request.account.LoginRequest;
import com.exe01.backend.dto.request.account.UpdateAccountRequest;
import com.exe01.backend.dto.response.JwtAuthenticationResponse;
import com.exe01.backend.entity.Account;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

public interface IAccountService extends IGenericService<AccountDTO> {

    <T>JwtAuthenticationResponse create(T signUpWitRoleRequest, String roleName) throws BaseException;

    Boolean update(UUID id, UpdateAccountRequest request) throws BaseException;

    Boolean delete(UUID id) throws BaseException;

    Boolean changeStatus(UUID id) throws BaseException;

    JwtAuthenticationResponse login(LoginRequest loginRequest) throws BaseException;

    Account findByUsername(String username) throws BaseException;

    String uploadAccountImage(UUID accountId, MultipartFile file) throws BaseException;

    byte[] downloadAccountImage(UUID username) throws BaseException;

    Boolean updatePoint(UUID id, Integer point) throws BaseException;

    Map<String, Object> getAccountMenteeInfo(UUID id) throws BaseException;

    PagingModel findAllForAdmin(String userName, String email, String role, String status, int page, int limit) throws BaseException;

    void  approveAccount(UUID id) throws BaseException;
    void rejectAccount(UUID id, String msssage) throws BaseException;

    JwtAuthenticationResponse loginWithGoogle(LoginRequest request) throws BaseException;

    int getPoint(UUID id) throws BaseException;
}
