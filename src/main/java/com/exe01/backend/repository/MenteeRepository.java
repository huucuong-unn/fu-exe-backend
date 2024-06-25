package com.exe01.backend.repository;

import com.exe01.backend.entity.Mentee;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenteeRepository extends JpaRepository<Mentee,UUID> {
    Optional<Mentee> findById(UUID id);

    List<Mentee> findAllByOrderByCreatedDate(Pageable pageable);

    List<Mentee> findAllByStatusOrderByCreatedDate(String status, Pageable pageable);

    int countByStatus(String status);

    @Query("SELECT ma.mentee FROM MentorApply ma JOIN ma.application a WHERE a.mentor.id = :mentorId")
    List<Mentee> findMenteesByMentorId(@Param("mentorId") Long mentorId);

    Optional<Mentee> findByStudentId(UUID studentId);

    @Query("SELECT COUNT(ma.mentee) FROM MentorApply ma JOIN ma.application a WHERE a.mentor.id = :mentorId")
    int countAllByMentorId(@Param("mentorId") UUID mentorId);
}
