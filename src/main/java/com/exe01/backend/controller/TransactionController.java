package com.exe01.backend.controller;

import com.exe01.backend.constant.ConstAPI;
import com.exe01.backend.dto.TransactionDTO;
import com.exe01.backend.dto.request.transaction.BaseTransactionRequest;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.service.ITransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin
@Slf4j
@Tag(name = "Transaction Controller")
public class TransactionController {

    @Autowired
    private ITransactionService transactionService;

    @Operation(summary = "Get all transaction", description = "API get all transaction")
    @GetMapping(value = ConstAPI.TransactionAPI.GET_TRANSACTION)
    public PagingModel getALl(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Getting all transaction with page: {}, limit: {}", page, limit);
        return transactionService.getAll(page, limit);
    }

    @Operation(summary = "Get all transaction with status active", description = "API get all transaction with status active")
    @GetMapping(value = ConstAPI.TransactionAPI.GET_TRANSACTION_STATUS_TRUE)
    public PagingModel findAllWithStatusActive(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Getting all active transaction with page: {}, limit: {}", page, limit);
        return transactionService.findAllByStatusTrue(page, limit);
    }

    @Operation(summary = "Get transaction by id", description = "API get transaction by id")
    @GetMapping(value = ConstAPI.TransactionAPI.GET_TRANSACTION_BY_ID + "{id}")
    public TransactionDTO findById(@PathVariable("id") UUID id) throws BaseException {
        log.info("Getting transaction with id: {}", id);
        return transactionService.findById(id);
    }

    @Operation(summary = "Create transaction", description = "API create new transaction")
    @PostMapping(value = ConstAPI.TransactionAPI.CREATE_TRANSACTION)
    public TransactionDTO create(@RequestBody BaseTransactionRequest request) throws BaseException {
        log.info("Creating new transaction with request: {}", request);
        return transactionService.create(request);
    }

    @Operation(summary = "Get all transaction with studentId", description = "API Get all transaction with studentId")
    @GetMapping(value = ConstAPI.TransactionAPI.GET_TRANSACTION_BY_ACCOUNT_ID_AND_SORT_BY_CREATED_DATE + "{studentId}")
    public PagingModel findAllByStudentId(@PathVariable("studentId") UUID studentId, @RequestParam(value = "createdDate") String createdDate ,@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Getting all transaction with studentId: {}, page: {}, limit: {}", studentId, page, limit);
        return transactionService.findAllByAccountIdAndSortByCreateDate(studentId,createdDate ,page, limit);
    }

}
