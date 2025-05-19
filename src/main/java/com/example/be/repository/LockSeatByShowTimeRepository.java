package com.example.be.repository;

import com.example.be.entity.LockSeatByShowTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LockSeatByShowTimeRepository extends JpaRepository<LockSeatByShowTime,Long> {
    Optional<LockSeatByShowTime> findBySeat_SeatIdAndShowtime_ShowtimeId(
            Long seatId, Long showtimeId
    );

    // xoá theo suất chiếu (dọn dẹp khi huỷ suất)
    void deleteAllByShowtime_ShowtimeId(Long showtimeId);
}
