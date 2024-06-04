package com.exe01.backend.repository;

import com.exe01.backend.entity.CampaignMentorProfile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CampaignMentorProfileRepository extends JpaRepository<CampaignMentorProfile, UUID> {
    Optional<CampaignMentorProfile> findById(UUID id);

    List<CampaignMentorProfile> findAllByOrderByCreatedDate(Pageable pageable);

    List<CampaignMentorProfile> findAllByStatusOrderByCreatedDate(String status, Pageable pageable);

    int countByStatus(String status);
}
