package com.asinglah.backend.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.asinglah.backend.Entity.User;

public interface UserRepository extends JpaRepository<User,Long> {


    @Query(value = "SELECT * FROM users u WHERE u.email_address = :email", nativeQuery = true)
    User findByEmailNative(@Param("email") String email);

    @Query(value = "SELECT * FROM users u WHERE u.email_address LIKE :email", nativeQuery = true)
    Optional<List<User>> searchUserByEmail(@Param("email") String email);



}
