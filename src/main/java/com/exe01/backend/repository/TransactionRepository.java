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
            "WHERE a.status = 'SUCCESS' " +
            "GROUP BY MONTH(a.createdDate)")
    List<Object[]> getMonthlyRevenue();

    @Query("SELECT t FROM Transaction t WHERE " +
            "(:email IS NULL OR :email = '' OR LOWER(t.account.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
            "(:status IS NULL OR :status = '' OR :status = t.status) ORDER BY " +
            "CASE WHEN :sortAmount = 'asc' THEN t.amount END ASC, " +
            "CASE WHEN :sortAmount = 'desc' THEN t.amount END DESC, " +
            "CASE WHEN :sortPoint = 'asc' THEN t.points END ASC, " +
            "CASE WHEN :sortPoint = 'desc' THEN t.points END DESC, " +
            "CASE WHEN :sortCreatedDate = 'asc' THEN t.createdDate END ASC, " +
            "CASE WHEN :sortCreatedDate = 'desc' THEN t.createdDate END DESC, " +
            "t.createdDate DESC")
    List<Transaction> findByEmailOrderByCreatedDateAndAmount(@Param("email") String email,
                                                             @Param("status") String status,
                                                             @Param("sortAmount") String sortAmount,
                                                             @Param("sortPoint") String sortPoint,
                                                             @Param("sortCreatedDate") String sortCreatedDate,
                                                             Pageable pageable);

}
