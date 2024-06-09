package com.exe01.backend.repository;

import com.exe01.backend.entity.Major;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MajorRepository extends JpaRepository<Major, UUID> {

    Optional<Major> findById(UUID id);

    List<Major> findAllByOrderByCreatedDate(Pageable pageable);

    List<Major> findAllByStatusOrderByCreatedDate(String status, Pageable pageable);

    int countByStatus(String status);

}
