package com.exe01.backend.repository;

import com.exe01.backend.entity.Role;
import com.exe01.backend.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    List<Role> findAllByOrderById(Pageable pageable);
    Role findById(Long id);

    List<Role> findAllByStatusTrueOrderById(Pageable pageable);
}
