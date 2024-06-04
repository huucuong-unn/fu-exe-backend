package com.exe01.backend.repository;

import com.exe01.backend.entity.MentorApply;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MentorApplyRepository extends JpaRepository<MentorApply, UUID> {

    List<MentorApply> findByMenteeIdAndStatus(UUID id, String status, Pageable pageable);

    List<MentorApply> findAllByOrderByCreatedDate(Pageable pageable);

    @Query("SELECT m FROM MentorApply m WHERE m.application.mentor.id = :id AND (COALESCE(:status, NULL) IS NULL OR m.status = :status)")
    List<MentorApply> findByApplicationMentorId(UUID id, String status, Pageable pageable);

    int countByStatus(String status);
}