package com.exe01.backend.repository;

import com.exe01.backend.entity.Application;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    List<Application> findByMentorId(UUID id, Pageable pageable);

    @Query("SELECT a FROM Application a " +
            "WHERE (:status = '' OR :status IS NULL OR a.status = :status) " +
            "AND (:companyId IS NULL OR a.mentor.company.id = :companyId) " +
            "AND (:mentorName IS NULL OR :mentorName = '' OR a.mentor.fullName LIKE %:mentorName%) " +
            "AND a.student.id = :studentId " +
            "ORDER BY " +
            "CASE WHEN :createdDate = 'asc' THEN a.createdDate END ASC, " +
            "CASE WHEN :createdDate = 'desc' THEN a.createdDate END DESC, " +
            "a.createdDate DESC")
    List<Application> findByStudentIdAndSearchSort(@Param("studentId") UUID studentId,
                                      @Param("companyId") UUID companyId,
                                      @Param("mentorName") String mentorName,
                                      @Param("status") String status,
                                      @Param("createdDate") String createdDate,
                                      Pageable pageable);
    List<Application> findAllByOrderByCreatedDate(Pageable pageable);

    List<Application> findAllByStatusOrderByCreatedDate(String status, Pageable pageable);

    @Query("SELECT a FROM Application a " +
            "JOIN CampaignMentorProfile cmp ON cmp.mentorProfile.mentor.id = a.mentor.id " +
            "WHERE (:status = 'ALL' OR a.status = :status) " +
            "AND a.mentor.id = :mentorId " +
            "AND cmp.campaign.status = :campaignStatus " +
            "ORDER BY " +
            "CASE WHEN :createdDate = 'asc' THEN a.createdDate END ASC, " +
            "CASE WHEN :createdDate = 'desc' THEN a.createdDate END DESC, " +
            "a.createdDate DESC")
    List<Application> findAllByMentorIdAndStatusAndSortByCreatedDate(
            @Param("mentorId") UUID mentorId,
            @Param("status") String status,
            @Param("campaignStatus") String campaignStatus,
            @Param("createdDate") String createdDate,
            Pageable pageable
    );
    int countByStatus(String status);

    int countAllByMentorCompanyId(UUID companyId);

    @Query("SELECT MONTH(a.createdDate) AS month, COUNT(a.id) AS applicationCount " +
            "FROM Application a " +
            "GROUP BY MONTH(a.createdDate) " )
            List<Object[]> getApplicationCountByMonth();
}
