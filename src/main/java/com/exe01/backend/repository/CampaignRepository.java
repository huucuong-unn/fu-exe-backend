package com.exe01.backend.repository;

import com.exe01.backend.entity.Campaign;
import com.exe01.backend.entity.Major;
import com.exe01.backend.exception.BaseException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, UUID> {

    Optional<Campaign> findById(UUID id);

    List<Campaign> findAllByOrderByCreatedDate(Pageable pageable);

    List<Campaign> findAllByStatusOrderByCreatedDate(String status, Pageable pageable);

    int countByStatus(String status);

}
