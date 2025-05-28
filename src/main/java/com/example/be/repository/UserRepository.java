package com.example.be.repository;

import com.example.be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Query("SELECT COUNT(u) FROM User u WHERE UPPER(u.role) <> 'ADMIN' AND UPPER(u.role) <> 'EMPLOYEE'")
    long countAllNonAdminNonEmployeeUsers();

    @Query("SELECT COUNT(u) FROM User u WHERE UPPER(u.role) <> 'ADMIN' AND UPPER(u.role) <> 'USER'")
    long countAllNonAdminNonUsers();

    @Query("SELECT u FROM User u WHERE UPPER(u.role) = UPPER(:role)")
    List<User> findAllByRole(@Param("role") String role);
    User findByEmail(String email);

}
