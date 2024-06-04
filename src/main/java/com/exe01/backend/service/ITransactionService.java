package com.exe01.backend.service;

import com.exe01.backend.dto.TransactionDTO;
import com.exe01.backend.dto.request.transaction.BaseTransactionRequest;
import com.exe01.backend.exception.BaseException;

public interface ITransactionService extends IGenericService<TransactionDTO> {

    TransactionDTO create(BaseTransactionRequest request) throws BaseException;

}
