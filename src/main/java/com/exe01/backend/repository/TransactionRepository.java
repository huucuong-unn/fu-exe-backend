package com.exe01.backend.repository;

import com.exe01.backend.entity.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    Optional<Transaction> findById(UUID id);

    List<Transaction> findAllByOrderByCreatedDate(Pageable pageable);

    List<Transaction> findAllByStatusOrderByCreatedDate(String status, Pageable pageable);

    int countByStatus(String status);

    @Query("SELECT a FROM Transaction a " +
            "WHERE a.account.id = :accountId " +
            "ORDER BY " +
            "CASE WHEN :createdDate = 'asc' THEN a.createdDate END ASC, " +
            "CASE WHEN :createdDate = 'desc' THEN a.createdDate END DESC, " +
            "a.createdDate DESC")
    List<Transaction> findAllByAccountIdAndSortByCreateDate(@Param("accountId") UUID accountId, @Param("createdDate") String createdDate, Pageable pageable);

    @Query("SELECT MONTH(a.createdDate) AS month, SUM(a.amount) AS revenue " +
            "FROM Transaction a " +
            "WHERE a.status = 'Success' " +
            "GROUP BY MONTH(a.createdDate)")
    List<Object[]> getMonthlyRevenue();
}
