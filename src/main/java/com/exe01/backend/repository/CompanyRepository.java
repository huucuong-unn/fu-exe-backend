package com.exe01.backend.repository;

import com.exe01.backend.entity.Company;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {

    Optional<Company> findById(UUID id);

    List<Company> findAllByOrderByCreatedDate(Pageable pageable);

    List<Company> findAllByStatusOrderByCreatedDate(String status, Pageable pageable);

    int countByStatus(String status);

    @Query("SELECT c FROM Company c " +
            "WHERE c.status = 'ACTIVE' " +
            "AND (:name IS NULL OR :name = '' OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:address IS NULL OR :address = '' OR LOWER(c.address) LIKE LOWER(CONCAT('%', :address, '%')))")
    List<Company> searchAndSortCompanies(@Param("name") String name,
                                         @Param("address") String address,
                                         Pageable pageable);

}
