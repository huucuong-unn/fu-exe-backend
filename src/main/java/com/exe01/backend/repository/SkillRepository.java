package com.exe01.backend.repository;

import com.exe01.backend.entity.Skill;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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

}
