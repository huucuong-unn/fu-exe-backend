package com.exe01.backend.repository;

import com.exe01.backend.entity.Account;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findById(UUID id);

    List<Account> findAllByOrderByCreatedDate(Pageable pageable);

    List<Account> findAllByStatusOrderByCreatedDate(String status, Pageable pageable);

    int countByStatus(String status);

    Optional<Account> findByUsername(String username);

    Optional<Account> findByEmail(String email);

    @Query("SELECT a FROM Account a WHERE " +
            "(:userName IS NULL OR :userName = '' OR LOWER(a.username) LIKE LOWER(CONCAT('%', :userName, '%'))) AND " +
            "(:email IS NULL OR :email = '' OR LOWER(a.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
            "(:role IS NULL OR :role = '' OR a.role.name = :role) AND " +
            "(:status IS NULL OR :status = '' OR a.status = :status) " +
            "ORDER BY a.createdDate DESC")
    List<Account> findAllForAdmin(@Param("userName") String userName,
                                  @Param("email") String email,
                                  @Param("role") String role,
                                  @Param("status") String status,
                                  Pageable pageable);
}
