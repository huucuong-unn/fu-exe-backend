package com.exe01.backend.repository;

import com.exe01.backend.entity.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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

}
