package com.exe01.backend.repository;

import com.exe01.backend.entity.University;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UniversityRepository extends JpaRepository<University, UUID> {

    Optional<University> findById(UUID id);

    List<University> findAllByOrderByCreatedDate(Pageable pageable);

    List<University> findAllByStatusOrderByCreatedDate(String status, Pageable pageable);

    int countByStatus(String status);
}
