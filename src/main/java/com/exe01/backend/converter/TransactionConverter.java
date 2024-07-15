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
        transactionDTO.setType(transaction.getType());
        transactionDTO.setStatus(transaction.getStatus());
        transactionDTO.setPoints(transaction.getPoints());
        transactionDTO.setAccount(AccountConverter.toDto(transaction.getAccount()));
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
        transaction.setType(transaction.getType());
        transaction.setStatus(transaction.getStatus());
        transaction.setPoints(transaction.getPoints());
        Account account = new Account();
        account.setId(transactionDTO.getAccount().getId());
        transaction.setAccount( account);
        transaction.setCreatedDate(transaction.getCreatedDate());
        transaction.setModifiedDate(transaction.getModifiedDate());
        transaction.setCreatedBy(transaction.getCreatedBy());
        transaction.setModifiedBy(transaction.getModifiedBy());

        return transaction;
    }

}
