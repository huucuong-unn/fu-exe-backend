package com.exe01.backend.repository;

import com.exe01.backend.entity.MentorApply;
import io.lettuce.core.dynamic.annotation.Param;
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

    @Query("SELECT m FROM MentorApply m WHERE m.mentee.student.name LIKE %:menteeName% AND m.application.mentor.fullName LIKE %:mentorFullName% AND (:campaignId IS NULL OR m.campaign.id = :campaignId) AND (:companyId IS NULL OR m.application.mentor.company = :companyId)")
    List<MentorApply> findAllByMenteeNameAndMentorFullNameAndCampaignId(
            @Param("menteeName") String menteeName,
            @Param("mentorFullName") String mentorFullName,
            @Param("campaignId") UUID campaignId,
            @Param("companyId") UUID companyId,
            Pageable pageable);

    int countByStatus(String status);
}