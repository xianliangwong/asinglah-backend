package com.asinglah.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.asinglah.backend.Entity.User;

public interface UserRepository extends JpaRepository<User,Long> {

}
