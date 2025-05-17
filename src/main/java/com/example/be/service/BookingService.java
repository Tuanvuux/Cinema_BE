package com.example.be.service;

import com.example.be.entity.Booking;
import com.example.be.enums.SeatStatus;
import com.example.be.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public void releaseExpiredSelections() {
        LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(5);
        List<Booking> expiredSelections = bookingRepository.findAllBySeatStatusAndBookingTimeBefore(SeatStatus.SELECTED, expirationTime);

        for (Booking selection : expiredSelections) {
            bookingRepository.delete(selection);
            // (optional) gửi thông báo websocket nếu có
        }
    }

    // Phương thức để lưu ghế khi người dùng chọn
    @Transactional
    public boolean lockSeat(Long showtimeId, Long seatId, Long userId) {
        Booking seatSelection = new Booking();
        seatSelection.setShowTimeId(showtimeId);
        seatSelection.setSeatId(seatId);
        seatSelection.setUserId(userId);
        seatSelection.setSeatStatus(SeatStatus.SELECTED);  // Đánh dấu ghế đã được chọn
        seatSelection.setBookingTime(LocalDateTime.now());

        // Lưu vào cơ sở dữ liệu
        bookingRepository.save(seatSelection);
        return true;
    }

    // Phương thức để hủy chọn ghế
    @Transactional
    public void unlockSeat(Long showtimeId, Long seatId) {
        Booking seatSelection = bookingRepository.findByShowTimeIdAndSeatId(showtimeId, seatId);
        if (seatSelection != null) {
            bookingRepository.delete(seatSelection);
        }
    }
    @Transactional
    public void deleteSeatSelection(Long userId, Long showTimeId, Long seatId, SeatStatus seatStatus) {
        bookingRepository.deleteByUserIdAndShowTimeIdAndSeatIdAndSeatStatus(userId, showTimeId, seatId, seatStatus);
    }
    public List<Long> getBookedSeatIds(Long showTimeId) {
        List<Booking> bookedSeats = bookingRepository.findByShowTimeIdAndSeatStatus(showTimeId, SeatStatus.BOOKED);
        return bookedSeats.stream()
                .map(Booking::getSeatId)
                .collect(Collectors.toList());
    }
    public void updateSeatStatus(String showtimeId, String seatId, String status, String userId) {
        String key = "seat:" + showtimeId + ":" + seatId;
        Map<String, String> seatData = new HashMap<>();
        seatData.put("status", status);
        seatData.put("userId", userId);
        seatData.put("timestamp", String.valueOf(System.currentTimeMillis()));

        redisTemplate.opsForHash().putAll(key, seatData);
        redisTemplate.expire(key, Duration.ofMinutes(30)); // TTL cho ghế đã chọn
    }

    public Map<String, String> getSeatStatusMap(String showtimeId, String seatId) {
        String key = "seat:" + showtimeId + ":" + seatId;
        return redisTemplate.<String, String>opsForHash().entries(key);
    }
}
