package com.example.be.repository;

import com.example.be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Query("SELECT COUNT(u) FROM User u WHERE UPPER(u.role) <> 'ADMIN'")
    long countAllNonAdminUsers();
}
