package com.example.be.repository;

import com.example.be.entity.Seat;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
}
