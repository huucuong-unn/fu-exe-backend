package com.exe01.backend.controller;

import com.exe01.backend.constant.ConstAPI;
import com.exe01.backend.dto.AccountDTO;
import com.exe01.backend.dto.request.account.CreateAccountRequest;
import com.exe01.backend.dto.request.account.UpdateAccountRequest;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.service.IAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin
@RestController
@Slf4j
@Tag(name = "Account Controller")
public class AccountController {

    @Autowired
    private IAccountService accountService;

    @Operation(summary = "Get all account", description = "API get all account")
    @GetMapping(value = ConstAPI.AccountAPI.GET_ACCOUNT)
    public PagingModel getALl(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        log.info("Getting all accounts with page: {}, limit: {}", page, limit);
        return accountService.getAll(page, limit);
    }

    @Operation(summary = "Get all account with status active", description = "API get all account with status active")
    @GetMapping(value = ConstAPI.AccountAPI.GET_ACCOUNT_STATUS_TRUE)
    public PagingModel findAllWithStatusActive(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        log.info("Getting all active accounts with page: {}, limit: {}", page, limit);
        return accountService.findAllByStatusTrue(page, limit);
    }

    @Operation(summary = "Get account by id", description = "API get account by id")
    @GetMapping(value = ConstAPI.AccountAPI.GET_ACCOUNT_BY_ID + "{id}")
    public AccountDTO findById(@PathVariable("id") UUID id) throws BaseException {
     try {
            log.info("Getting account with id: {}", id);
            return accountService.findById(id);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new BaseException(500, e.getMessage(), "Internal Server Error");
        }
    }

    @Operation(summary = "Create role", description = "API create new account")
    @PostMapping(value = ConstAPI.AccountAPI.CREATE_ACCOUNT)
    public AccountDTO create(@RequestBody CreateAccountRequest request) {
        log.info("Creating new account with request: {}", request);
        return accountService.create(request);
    }

    @Operation(summary = "Update role", description = "API update account")
    @PutMapping(value = ConstAPI.AccountAPI.UPDATE_ACCOUNT + "{id}")
    public Boolean update(@PathVariable("id") UUID id, @RequestBody UpdateAccountRequest request) {
        log.info("Updating account with id: {}, request: {}", id, request);
        return accountService.update(id, request);
    }

    @Operation(summary = "Delete role", description = "API delete account")
    @DeleteMapping(value = ConstAPI.AccountAPI.DELETE_ACCOUNT + "{id}")
    public Boolean delete(@PathVariable("id") UUID id) {
        log.info("Deleted account with id: {}", id);
        return accountService.delete(id);
    }
}
