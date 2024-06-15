package com.exe01.backend.repository;

import com.exe01.backend.dto.response.skill.AllSkillOfCompanyResponse;
import com.exe01.backend.entity.Skill;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SkillRepository extends JpaRepository<Skill, UUID> {

    Optional<Skill> findById(UUID id);

    List<Skill> findAllByOrderByCreatedDate(Pageable pageable);

    List<Skill> findAllByStatusOrderByCreatedDate(String status, Pageable pageable);

    int countByStatus(String status);


    @Query("SELECT DISTINCT new com.exe01.backend.dto.response.skill.AllSkillOfCompanyResponse(s.name) " +
            "FROM Skill s " +
            "JOIN SkillMentorProfile smp ON s.id = smp.skill.id " +
            "JOIN MentorProfile mp ON smp.mentorProfile.id = mp.id " +
            "JOIN Mentor m ON mp.mentor.id = m.id " +
            "WHERE m.company.id = :companyId")
    List<AllSkillOfCompanyResponse> findDistinctSkillsByCompanyId(@Param("companyId") UUID companyId);
}
