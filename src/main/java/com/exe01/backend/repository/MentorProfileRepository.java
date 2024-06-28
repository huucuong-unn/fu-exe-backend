package com.exe01.backend.repository;

import com.exe01.backend.entity.Mentor;
import com.exe01.backend.entity.MentorProfile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MentorProfileRepository extends JpaRepository<MentorProfile, UUID> {

    Optional<MentorProfile> findById(UUID id);

    List<MentorProfile> findAllByOrderByCreatedDate(Pageable pageable);

    @Query(value = "SELECT * FROM mentor_profile_tbl WHERE status = 'USING' ORDER BY created_date", nativeQuery = true)
    List<MentorProfile> findAllBy(Pageable pageable);

    List<MentorProfile> findAllByStatusOrderByCreatedDate(String status, Pageable pageable);

    @Query(value = "SELECT * FROM mentor_profile_tbl WHERE mentor_id = :mentorId AND status = 'ACTIVE'", nativeQuery = true)
    MentorProfile findAllByMentorId(@Param("mentorId") UUID mentorId);

    @Query(value = "SELECT mp.* FROM mentor_profile_tbl mp " +
            "INNER JOIN mentor_tbl m ON mp.mentor_id = m.id " +
            "WHERE m.company_id = :companyId", nativeQuery = true)
    List<MentorProfile> findByCompanyId(@Param("companyId") UUID id);

    @Query("SELECT  cmp.mentorProfile  FROM CampaignMentorProfile cmp  WHERE cmp.campaign.status != :campaignStatus AND cmp.mentorProfile.mentor.company.id = :companyId and cmp.mentorProfile.status = :mentorProfileStatus AND cmp.mentorProfile.mentor.id != :mentorId")
    List<MentorProfile> findAllByMentorProfilesStatusAndCompanyId(@Param("companyId") UUID companyId, @Param("mentorId") UUID mentorId ,@Param("campaignStatus") String campaignStatus, @Param("mentorProfileStatus") String mentorProfileStatus);

    @Query("SELECT  cmp.mentorProfile  FROM CampaignMentorProfile cmp JOIN Application a ON cmp.mentorProfile.mentor.id = a.mentor.id WHERE cmp.campaign.id = a.mentorApply.campaign.id AND a.student.id = :studentId")
    List<MentorProfile> findAllByMenteeId(@Param("studentId") UUID studentId);

    @Query("SELECT mp FROM CampaignMentorProfile cmp RIGHT JOIN MentorProfile mp ON cmp.mentorProfile.id = mp.id " +
            "WHERE (:companyId IS NULL OR mp.mentor.company.id = :companyId) " +
            "AND mp.status = 'USING' " +
            "AND (:mentorName IS NULL OR :mentorName ='' OR LOWER(mp.mentor.fullName) LIKE LOWER(CONCAT('%', :mentorName, '%')))")
    List<MentorProfile> findAllByMentorProfilesForAdminSearch(
            @Param("companyId") UUID companyId,
            @Param("mentorName") String mentorName,
            Pageable pageable);
    int countByStatus(String status);


}
