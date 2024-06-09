package com.exe01.backend.repository;

import com.exe01.backend.entity.Mentee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenteeRepository extends JpaRepository<Mentee,UUID> {
    Optional<Mentee> findById(UUID id);

    List<Mentee> findAllByOrderByCreatedDate(Pageable pageable);

    List<Mentee> findAllByStatusOrderByCreatedDate(String status, Pageable pageable);

    int countByStatus(String status);
}
