package com.exe01.backend.repository;

import com.exe01.backend.entity.MentorProfile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MentorProfileRepository extends JpaRepository<MentorProfile, UUID> {

    Optional<MentorProfile> findById(UUID id);

    List<MentorProfile> findAllByOrderByCreatedDate(Pageable pageable);

    List<MentorProfile> findAllByStatusOrderByCreatedDate(String status, Pageable pageable);

}
