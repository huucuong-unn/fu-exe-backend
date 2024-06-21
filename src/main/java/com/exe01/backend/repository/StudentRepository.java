package com.exe01.backend.repository;

import com.exe01.backend.entity.Student;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {

    Optional<Student> findById(UUID id);

    List<Student> findAllByOrderByCreatedDate(Pageable pageable);

    List<Student> findAllByStatusOrderByCreatedDate(String status, Pageable pageable);

    int countByStatus(String status);

    Optional<Student> findByAccountId(UUID accountId);
}
