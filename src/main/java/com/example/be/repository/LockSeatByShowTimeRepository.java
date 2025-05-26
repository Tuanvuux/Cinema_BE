package com.example.be.repository;

import com.example.be.entity.LockSeatByShowTime;
import com.example.be.entity.Seat;
import com.example.be.entity.ShowTime;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LockSeatByShowTimeRepository extends JpaRepository<LockSeatByShowTime,Long> {
    @Query("SELECT l.seat.seatId FROM LockSeatByShowTime l WHERE l.showtime.showtimeId = :showtimeId AND l.status = 'INVALID'")
    List<Long> findSeatIdsByShowtimeIdAndStatusInvalid(@Param("showtimeId") long showtimeId);


}
