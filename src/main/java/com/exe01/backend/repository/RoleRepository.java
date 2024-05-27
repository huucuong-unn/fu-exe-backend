package com.exe01.backend.repository;

import com.exe01.backend.entity.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findById(UUID id);

    List<Role> findAllBy(Pageable pageable);

    List<Role> findAllByStatusOrderByCreatedDate(String status, Pageable pageable);

}
