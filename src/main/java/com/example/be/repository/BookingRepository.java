package com.example.be.repository;

import com.example.be.entity.Booking;
import com.example.be.enums.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllBySeatStatusAndBookingTimeBefore(SeatStatus seatStatus, LocalDateTime time);
    boolean existsByShowTimeIdAndSeatIdAndSeatStatus(Long showtimeId, Long seatId, SeatStatus seatStatus);
    Booking findByShowTimeIdAndSeatId(Long showtimeId, Long seatId);
    void deleteByUserIdAndShowTimeIdAndSeatIdAndSeatStatus(Long userId, Long showTimeId, Long seatId, SeatStatus seatStatus);
    List<Booking> findByShowTimeId(Long showTimeId);
    List<Booking> findByShowTimeIdAndSeatStatus(Long showTimeId, SeatStatus seatStatus);
    List<Booking> findByUserIdAndShowTimeIdAndSeatStatus(Long userId, Long showTimeId, SeatStatus seatStatus);
    boolean existsByShowTimeId(Long showTimeId);
    boolean existsBySeatId(Long seatId);

    @Query("""
    SELECT CASE WHEN COUNT(st) > 0 THEN true ELSE false END
    FROM ShowTime st
    WHERE st.room.id = :roomId AND st.showDate >= CURRENT_DATE
    """)
    boolean existsShowtimeByRoomId(@Param("roomId") Long roomId);

    // Kiểm tra phòng có lịch chiếu và lịch chiếu đó có vé đặt rồi hay chưa
    @Query("""
        SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
        FROM Booking b
        JOIN b.showtime st
        WHERE st.room.id = :roomId AND (b.seatStatus = 'SELECTED' OR b.seatStatus = 'BOOKED')
    """)
    boolean existsBookingByRoomIdWithBookedSeats(@Param("roomId") Long roomId);
}

