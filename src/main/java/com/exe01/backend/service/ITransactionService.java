package com.exe01.backend.service;

import com.exe01.backend.dto.Dashboard.MonthlyRevenue;
import com.exe01.backend.dto.TransactionDTO;
import com.exe01.backend.dto.request.transaction.BaseTransactionRequest;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ITransactionService extends IGenericService<TransactionDTO> {

    TransactionDTO create(BaseTransactionRequest request) throws BaseException;

    Boolean changeStatus(UUID id) throws BaseException;

     PagingModel findAllByAccountIdAndSortByCreateDate(UUID accountId, String createdDate, int page, int size) throws BaseException;

     PagingModel findByEmailOrderByCreatedDateAndAmount(String email,String status ,String sortAmount, String sortPoint, String sortCreatedDate, int page, int size) throws BaseException;

     List<MonthlyRevenue> getMonthlyRevenue() throws BaseException;
}
