package com.example.be.service;

import com.example.be.dto.response.SeatStatusResponse;
import com.example.be.entity.Booking;
import com.example.be.enums.SeatStatus;
import com.example.be.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Transactional
    public void releaseExpiredSelections() {
        LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(5);
        List<Booking> expiredSelections = bookingRepository.findAllBySeatStatusAndBookingTimeBefore(SeatStatus.SELECTED, expirationTime);

        for (Booking selection : expiredSelections) {
            bookingRepository.delete(selection);
            // Gửi thông báo WebSocket ghế được mở khóa
            messagingTemplate.convertAndSend("/topic/seats/" + selection.getShowTimeId(),
                    new SeatStatusResponse("Ghế hết hạn giữ", selection.getSeatId(), SeatStatus.AVAILABLE, selection.getUserId()));
        }
    }

    @Transactional
    public boolean lockSeat(Long showtimeId, Long seatId, Long userId) {
        Booking seatSelection = new Booking();
        seatSelection.setShowTimeId(showtimeId);
        seatSelection.setSeatId(seatId);
        seatSelection.setUserId(userId);
        seatSelection.setSeatStatus(SeatStatus.SELECTED);
        seatSelection.setBookingTime(LocalDateTime.now());

        bookingRepository.save(seatSelection);
        return true;
    }

    @Transactional
    public void unlockSeat(Long showtimeId, Long seatId) {
        Booking seatSelection = bookingRepository.findByShowTimeIdAndSeatId(showtimeId, seatId);

        if (seatSelection != null && seatSelection.getSeatStatus() == SeatStatus.SELECTED) {
            bookingRepository.delete(seatSelection);

            // Xóa Redis
            String key = "seat:" + showtimeId + ":" + seatId;
            redisTemplate.delete(key);

            // Gửi WebSocket: chỉ gửi nếu thực sự unlock
            messagingTemplate.convertAndSend("/topic/seats/" + showtimeId,
                    new SeatStatusResponse("Ghế tự động mở do hết hạn", seatId, SeatStatus.AVAILABLE, seatSelection.getUserId()));

        } else {
            System.out.println("⚠️ Không unlock ghế " + seatId + " vì không ở trạng thái SELECTED.");
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
        redisTemplate.expire(key, Duration.ofMinutes(30));
    }

    public Map<String, String> getSeatStatusMap(String showtimeId, String seatId) {
        String key = "seat:" + showtimeId + ":" + seatId;
        return redisTemplate.<String, String>opsForHash().entries(key);
    }

    @Transactional
    public void bookSeats(Long userId, Long showtimeId, List<Long> seatIds) {
        for (Long seatId : seatIds) {
            Booking booking = bookingRepository.findByShowTimeIdAndSeatId(showtimeId, seatId);
            if (booking != null) {
                booking.setSeatStatus(SeatStatus.BOOKED);
                booking.setBookingTime(LocalDateTime.now());
                bookingRepository.save(booking);

                messagingTemplate.convertAndSend("/topic/seats/" + showtimeId,
                        new SeatStatusResponse("Ghế đã được đặt", seatId, SeatStatus.BOOKED, userId));

                // Xóa trạng thái giữ trong Redis
                String key = "seat:" + showtimeId + ":" + seatId;
                redisTemplate.delete(key);
            }
        }
    }
}
