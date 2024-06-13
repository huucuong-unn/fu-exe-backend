package com.exe01.backend.repository;

import com.exe01.backend.entity.SkillMentorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SkillMentorProfileRepository extends JpaRepository<SkillMentorProfile, UUID> {

    @Query(value = "SELECT * FROM skill_mentor_profile_tbl WHERE mentor_profile_id = :mentorProfileId", nativeQuery = true)
    List<SkillMentorProfile> findAllByMentorProfileId(@Param("mentorProfileId") UUID mentorProfileId);

}
