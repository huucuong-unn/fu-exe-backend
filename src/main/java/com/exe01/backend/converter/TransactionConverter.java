package com.exe01.backend.converter;

import com.exe01.backend.dto.TransactionDTO;
import com.exe01.backend.entity.Account;
import com.exe01.backend.entity.Transaction;
import com.exe01.backend.exception.BaseException;

public class TransactionConverter {

    public static TransactionDTO toDto(Transaction transaction) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(transaction.getId());
        transactionDTO.setAmount(transaction.getAmount());
        transactionDTO.setStatus(transaction.getStatus());
        transactionDTO.setPoints(transaction.getPoints());
        transactionDTO.setAccountId(transaction.getAccount().getId());
        transactionDTO.setCreatedDate(transaction.getCreatedDate());
        transactionDTO.setModifiedDate(transaction.getModifiedDate());
        transactionDTO.setCreatedBy(transaction.getCreatedBy());
        transactionDTO.setModifiedBy(transaction.getModifiedBy());

        return transactionDTO;
    }

    public static Transaction toEntity(TransactionDTO transactionDTO) throws BaseException {
        Transaction transaction = new Transaction();
        transaction.setId(transactionDTO.getId());
        transaction.setAmount(transaction.getAmount());
        transaction.setStatus(transaction.getStatus());
        transaction.setPoints(transaction.getPoints());
        Account account = new Account();
        account.setId(transactionDTO.getAccountId());
        transaction.setAccount( account);
        transaction.setCreatedDate(transaction.getCreatedDate());
        transaction.setModifiedDate(transaction.getModifiedDate());
        transaction.setCreatedBy(transaction.getCreatedBy());
        transaction.setModifiedBy(transaction.getModifiedBy());

        return transaction;
    }

}
