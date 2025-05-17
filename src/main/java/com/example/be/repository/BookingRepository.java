package com.example.be.repository;

import com.example.be.entity.Booking;
import com.example.be.enums.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


    public interface BookingRepository extends JpaRepository<Booking, Long> {
        List<Booking> findAllBySeatStatusAndBookingTimeBefore(SeatStatus seatStatus, LocalDateTime time);
        boolean existsByShowTimeIdAndSeatIdAndSeatStatus(Long showtimeId, Long seatId, SeatStatus seatStatus);
        Booking findByShowTimeIdAndSeatId(Long showtimeId, Long seatId);
        void deleteByUserIdAndShowTimeIdAndSeatIdAndSeatStatus(Long userId, Long showTimeId, Long seatId, SeatStatus seatStatus);
        List<Booking> findByShowTimeId(Long showTimeId);
        List<Booking> findByShowTimeIdAndSeatStatus(Long showTimeId, SeatStatus seatStatus);

    }

