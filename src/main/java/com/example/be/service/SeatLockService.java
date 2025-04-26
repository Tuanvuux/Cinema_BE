package com.example.be.service;

import com.example.be.entity.SeatSelection;
import com.example.be.enums.SeatStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SeatLockService {

    private final StringRedisTemplate redisTemplate;
    private final SeatSelectionService seatSelectionService;  // Inject service xử lý lưu ghế

    // Khóa ghế
    public boolean lockSeat(Long showtimeId, Long seatId, Long userId) {
        String key = "seat-lock:" + showtimeId + ":" + seatId;

        // Kiểm tra khóa ghế với Redis
        Boolean success = redisTemplate.opsForValue().setIfAbsent(key, userId.toString(), 5, TimeUnit.MINUTES);
        if (Boolean.TRUE.equals(success)) {
            // Lưu thông tin ghế vào bảng seat_selection khi khóa thành công
            seatSelectionService.lockSeat(showtimeId, seatId, userId);
            return true;
        }
        return false;
    }

    // Mở khóa ghế
    public void unlockSeat(Long showtimeId, Long seatId) {
        String key = "seat-lock:" + showtimeId + ":" + seatId;
        redisTemplate.delete(key);  // Xóa khóa trong Redis

        // Xóa ghế khỏi bảng seat_selection
        seatSelectionService.unlockSeat(showtimeId, seatId);
    }

    // Kiểm tra trạng thái ghế
    public boolean isSeatLocked(Long showtimeId, Long seatId) {
        String key = "seat-lock:" + showtimeId + ":" + seatId;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
