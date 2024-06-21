package com.exe01.backend.repository;

import com.exe01.backend.entity.Mentor;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MentorRepository extends JpaRepository<Mentor, UUID> {

    Optional<Mentor> findById(UUID id);

    List<Mentor> findAllByOrderByCreatedDate(Pageable pageable);

    List<Mentor> findAllByStatusOrderByCreatedDate(String status, Pageable pageable);

    int countByStatus(String status);

}
