package com.asinglah.backend.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.asinglah.backend.Entity.StatusCode;


public interface StatusCodeRepository extends JpaRepository<StatusCode,Long> {


    @Query(value = "SELECT * FROM status_code s where s.status_desc=:desc", nativeQuery = true)
    Optional<StatusCode> findByStatusDesc(@Param("desc") String desc);
}
