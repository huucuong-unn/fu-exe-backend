package com.exe01.backend.controller;

import com.exe01.backend.constant.ConstAPI;
import com.exe01.backend.dto.AccountDTO;
import com.exe01.backend.dto.request.account.CreateAccountRequest;
import com.exe01.backend.dto.request.account.UpdateAccountRequest;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin
@RestController
public class AccountController {
    @Autowired
    private IAccountService accountService;

    @GetMapping(value = ConstAPI.AccountAPI.GET_ACCOUNT)
    public PagingModel getALl(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        return accountService.getAll(page, limit);
    }

    @GetMapping(value = ConstAPI.AccountAPI.GET_ACCOUNT_STATUS_TRUE)
    public PagingModel findAllWithStatusActive(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        return accountService.findAllByStatusTrue(page, limit);
    }

    @GetMapping(value = ConstAPI.AccountAPI.GET_ACCOUNT_BY_ID + "{id}")
    public AccountDTO findById(@PathVariable("id") UUID id) {
        return accountService.findById(id);
    }

    @PostMapping(value = ConstAPI.AccountAPI.CREATE_ACCOUNT)
    public AccountDTO create(@RequestBody CreateAccountRequest request) {
        return accountService.create(request);
    }

    @PutMapping(value = ConstAPI.AccountAPI.UPDATE_ACCOUNT + "{id}")
    public Boolean update(@PathVariable("id") UUID id, @RequestBody UpdateAccountRequest request) {
        return accountService.update(id, request);
    }

    @DeleteMapping(value = ConstAPI.AccountAPI.DELETE_ACCOUNT + "{id}")
    public Boolean delete(@PathVariable("id") UUID id) {
        return accountService.delete(id);
    }
}
