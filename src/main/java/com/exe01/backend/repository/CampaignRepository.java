package com.exe01.backend.repository;

import com.exe01.backend.entity.Campaign;
import com.exe01.backend.entity.Major;
import com.exe01.backend.exception.BaseException;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, UUID> {

    Optional<Campaign> findById(UUID id);

    List<Campaign> findAllByOrderByCreatedDate(Pageable pageable);

    List<Campaign> findAllByStatusOrderByCreatedDate(String status, Pageable pageable);

    @Query("SELECT a FROM Campaign a WHERE ( :campaignName IS NULL OR :campaignName ='' OR LOWER(a.name) LIKE LOWER(CONCAT('%', :campaignName, '%')) ) AND ( :status IS NULL OR :status = '' OR a.status = :status)" )
    List<Campaign> findAllCampaignForAdminSearch(@Param("campaignName") String campaignName, String status , Pageable pageable) throws BaseException;

    int countByStatus(String status);
}
