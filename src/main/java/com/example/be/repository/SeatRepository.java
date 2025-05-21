package com.example.be.repository;

import com.example.be.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> getSeatsByRoomId(Long roomId);
    long count();

    @Query("SELECT s FROM Seat s WHERE s.room.id = (SELECT st.room.id FROM ShowTime st WHERE st.showtimeId = :showtimeId)")
    List<Seat> findSeatsByShowtimeId(@Param("showtimeId") Long showtimeId);
}
