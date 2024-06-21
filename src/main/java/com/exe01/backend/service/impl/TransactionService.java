package com.exe01.backend.service.impl;

import com.exe01.backend.constant.ConstError;
import com.exe01.backend.constant.ConstStatus;
import com.exe01.backend.converter.AccountConverter;
import com.exe01.backend.converter.TransactionConverter;
import com.exe01.backend.dto.TransactionDTO;
import com.exe01.backend.dto.request.transaction.BaseTransactionRequest;
import com.exe01.backend.entity.Account;
import com.exe01.backend.entity.Transaction;
import com.exe01.backend.enums.ErrorCode;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.repository.TransactionRepository;
import com.exe01.backend.service.IAccountService;
import com.exe01.backend.service.ITransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.exe01.backend.constant.ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_TRANSACTION;

@Service
public class TransactionService implements ITransactionService {

    Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private IAccountService accountService;

    @Override
    public TransactionDTO findById(UUID id) throws BaseException {
        try {
            logger.info("Find transaction by id {}", id);
            String hashKey = HASH_KEY_PREFIX_FOR_TRANSACTION + id.toString();
            TransactionDTO transactionDTO = (TransactionDTO) redisTemplate.opsForHash().get(HASH_KEY_PREFIX_FOR_TRANSACTION, hashKey);

            if (!Objects.isNull(transactionDTO)) {
                return transactionDTO;
            }

            Optional<Transaction> transactionById = transactionRepository.findById(id);
            boolean isTransactionExist = transactionById.isPresent();

            if (!isTransactionExist) {
                logger.warn("Transaction with id {} not found", id);
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Transaction.TRANSACTION_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            transactionDTO = TransactionConverter.toDto(transactionById.get());

            redisTemplate.opsForHash().put(HASH_KEY_PREFIX_FOR_TRANSACTION, hashKey, transactionDTO);

            return transactionDTO;

        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public PagingModel getAll(Integer page, Integer limit) throws BaseException {
        try {
            logger.info("Get all transaction with paging");
            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            String cacheKey = HASH_KEY_PREFIX_FOR_TRANSACTION + "all:" + page + ":" + limit;

            List<TransactionDTO> transactionDTOs;

            if (redisTemplate.opsForHash().hasKey(HASH_KEY_PREFIX_FOR_TRANSACTION, cacheKey)) {
                logger.info("Fetching transaction from cache for page {} and limit {}", page, limit);
                transactionDTOs = (List<TransactionDTO>) redisTemplate.opsForHash().get(HASH_KEY_PREFIX_FOR_TRANSACTION, cacheKey);
            } else {
                logger.info("Fetching students from database for page {} and limit {}", page, limit);
                List<Transaction> transactions = transactionRepository.findAllByOrderByCreatedDate(pageable);
                transactionDTOs = transactions.stream().map(TransactionConverter::toDto).toList();

                redisTemplate.opsForHash().put(HASH_KEY_PREFIX_FOR_TRANSACTION, cacheKey, transactionDTOs);
            }


            result.setListResult(transactionDTOs);
            result.setTotalPage(((int) Math.ceil((double) (totalItem()) / limit)));
            result.setLimit(limit);

            return result;
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    public int totalItem() {
        return (int) transactionRepository.count();
    }

    private int totalItemWithStatusActive() {
        return transactionRepository.countByStatus(ConstStatus.TransactionStatus.SUCCESS_STATUS);
    }

    @Override
    public PagingModel findAllByStatusTrue(Integer page, Integer limit) throws BaseException {
        try {
            logger.info("Get all transaction with status active");
            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            String cacheKey = HASH_KEY_PREFIX_FOR_TRANSACTION + "all:" + "active:" + page + ":" + limit;

            List<TransactionDTO> transactionDTOs;

            if (redisTemplate.opsForHash().hasKey(HASH_KEY_PREFIX_FOR_TRANSACTION, cacheKey)) {
                logger.info("Fetching transaction from cache for page {} and limit {}", page, limit);
                transactionDTOs = (List<TransactionDTO>) redisTemplate.opsForHash().get(HASH_KEY_PREFIX_FOR_TRANSACTION, cacheKey);
            } else {
                logger.info("Fetching students from database for page {} and limit {}", page, limit);
                List<Transaction> transactions = transactionRepository.findAllByStatusOrderByCreatedDate(ConstStatus.TransactionStatus.SUCCESS_STATUS, pageable);
                transactionDTOs = transactions.stream().map(TransactionConverter::toDto).toList();

                redisTemplate.opsForHash().put(HASH_KEY_PREFIX_FOR_TRANSACTION, cacheKey, transactionDTOs);
            }


            result.setListResult(transactionDTOs);
            result.setTotalPage(((int) Math.ceil((double) (totalItemWithStatusActive()) / limit)));
            result.setLimit(limit);

            return result;
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public TransactionDTO create(BaseTransactionRequest request) throws BaseException {
        try {
            logger.info("Create transaction");
            Transaction transaction = new Transaction();
            transaction.setAmount(request.getAmount());
            transaction.setPoints(request.getPoints());

            Account accountById = AccountConverter.toEntity(accountService.findById(request.getAccountId()));

            if (accountById.getStatus().equals(ConstStatus.INACTIVE_STATUS)) {
                logger.warn("Transaction with id {} not found", request.getAccountId());
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Account.ACCOUNT_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            transaction.setAccount(accountById);
            transaction.setStatus(ConstStatus.TransactionStatus.SUCCESS_STATUS);

            transactionRepository.save(transaction);

            Set<String> keysToDelete = redisTemplate.keys("Transaction:*");
            if (keysToDelete != null && !keysToDelete.isEmpty()) {
                redisTemplate.delete(keysToDelete);
            }

            return TransactionConverter.toDto(transaction);

        } catch (Exception baseException) {
            if (baseException instanceof BaseException) {
                throw baseException;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public Boolean changeStatus(UUID id) throws BaseException {
        try {
            logger.info("Change status transaction with id {}", id);
            Optional<Transaction> transactionById = transactionRepository.findById(id);
            boolean isTransactionExist = transactionById.isPresent();

            if (!isTransactionExist) {
                logger.warn("Transaction with id {} not found", id);
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Transaction.TRANSACTION_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            Transaction transaction = transactionById.get();
            transaction.setStatus(ConstStatus.TransactionStatus.FAILED_STATUS);

            transactionRepository.save(transaction);

            Set<String> keysToDelete = redisTemplate.keys("Transaction:*");
            if (keysToDelete != null && !keysToDelete.isEmpty()) {
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
    public PagingModel findAllByAccountIdAndSortByCreateDate(UUID accountId, String createdDate, int page, int limit) throws BaseException {

        try {
            logger.info("Get all transaction with status active");
            PagingModel result = new PagingModel();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            String cacheKey = HASH_KEY_PREFIX_FOR_TRANSACTION + "all:" + accountId + createdDate + page + ":" + limit;

            List<TransactionDTO> transactionDTOs;

            if (redisTemplate.opsForHash().hasKey(HASH_KEY_PREFIX_FOR_TRANSACTION, cacheKey)) {
                logger.info("Fetching transaction from cache for page {} and limit {}", page, limit);
                transactionDTOs = (List<TransactionDTO>) redisTemplate.opsForHash().get(HASH_KEY_PREFIX_FOR_TRANSACTION, cacheKey);
            } else {
                logger.info("Fetching students from database for page {} and limit {}", page, limit);
                List<Transaction> transactions = transactionRepository.findAllByAccountIdAndSortByCreateDate(accountId, createdDate, pageable);
                transactionDTOs = transactions.stream().map(TransactionConverter::toDto).toList();

                redisTemplate.opsForHash().put(HASH_KEY_PREFIX_FOR_TRANSACTION, cacheKey, transactionDTOs);
            }


            result.setListResult(transactionDTOs);
            result.setTotalPage(((int) Math.ceil((double) (totalItemWithStatusActive()) / limit)));
            result.setLimit(limit);

            return result;
        } catch (Exception baseException) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), baseException.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }
}
