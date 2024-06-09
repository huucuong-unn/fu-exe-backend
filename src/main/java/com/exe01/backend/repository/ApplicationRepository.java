package com.exe01.backend.repository;

import com.exe01.backend.entity.Application;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    List<Application> findByMentorId(UUID id, Pageable pageable);

    List<Application> findByStudentId(UUID id, Pageable pageable);

    List<Application> findAllByOrderByCreatedDate(Pageable pageable);

    List<Application> findAllByStatusOrderByCreatedDate(String status, Pageable pageable);

    int countByStatus(String status);

}
