package com.example.be.repository;

import com.example.be.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> getSeatsByRoomId(Long roomId);
    long count();
}
