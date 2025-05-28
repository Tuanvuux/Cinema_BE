package com.example.be.service;

import com.example.be.dto.response.LockedSeatDTO;
import com.example.be.entity.LockSeatByShowTime;
import com.example.be.repository.LockSeatByShowTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SeatLockService {

    private final StringRedisTemplate redisTemplate;
    private final BookingService bookingService;

    private String buildKey(Long showtimeId, Long seatId) {
        return "seat:" + showtimeId + ":" + seatId;
    }

    private String buildSetKey(Long showtimeId) {
        return "seat-locked-keys:" + showtimeId;
    }

    // ✅ Giữ ghế (kèm lưu vào Set)
    public boolean lockSeat(Long showtimeId, Long seatId, Long userId) {
        String key = buildKey(showtimeId, seatId);
        String setKey = buildSetKey(showtimeId);

        // Kiểm tra xem ghế đã bị khóa chưa
        Boolean success = redisTemplate.opsForValue().setIfAbsent(key, userId.toString(), 3, TimeUnit.MINUTES);
        if (Boolean.TRUE.equals(success)) {
            // ✅ Thêm key vào Redis Set để theo dõi ghế bị khóa
            redisTemplate.opsForSet().add(setKey, key);
            redisTemplate.expire(setKey, 10, TimeUnit.MINUTES); // Set sẽ hết hạn sau 10 phút

            // ✅ Gọi service lưu DB
            bookingService.lockSeat(showtimeId, seatId, userId);
            return true;
        }
        return false;
    }

    // ✅ Mở ghế (xoá key Redis và xoá khỏi Redis Set)
    public void unlockSeat(Long showtimeId, Long seatId) {
        String key = buildKey(showtimeId, seatId);
        String setKey = buildSetKey(showtimeId);

        redisTemplate.delete(key);
        redisTemplate.opsForSet().remove(setKey, key);

        bookingService.unlockSeat(showtimeId, seatId);
    }

    // ✅ Kiểm tra ghế có bị giữ không
    public boolean isSeatLocked(Long showtimeId, Long seatId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(buildKey(showtimeId, seatId)));
    }

    // ✅ Lấy danh sách các seatId đang bị giữ cho 1 suất chiếu
    public List<LockedSeatDTO> getLockedSeats(Long showtimeId) {
        String setKey = buildSetKey(showtimeId);
        Set<String> lockedKeys = redisTemplate.opsForSet().members(setKey);

        if (lockedKeys == null || lockedKeys.isEmpty()) return Collections.emptyList();

        List<LockedSeatDTO> result = new ArrayList<>();

        for (String key : lockedKeys) {
            if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
                String[] parts = key.split(":"); // seat:showtimeId:seatId
                if (parts.length == 3) {
                    Long seatId = Long.parseLong(parts[2]);
                    String userIdStr = redisTemplate.opsForValue().get(key);
                    if (userIdStr != null) {
                        Long userId = Long.parseLong(userIdStr);
                        result.add(new LockedSeatDTO(seatId, userId));
                    }
                }
            } else {
                // Nếu key hết hạn nhưng vẫn còn trong Set → xóa khỏi set
                redisTemplate.opsForSet().remove(setKey, key);
            }
        }

        return result;
    }
    public List<Long> extendSeatLocks(Long showtimeId, Long userId, List<Long> seatIds) {
        List<Long> extendedSeats = new ArrayList<>();

        for (Long seatId : seatIds) {
            String key = buildKey(showtimeId, seatId);
            String currentUserId = redisTemplate.opsForValue().get(key);

            if (currentUserId != null && currentUserId.equals(userId.toString())) {
                Boolean result = redisTemplate.expire(key, 5, TimeUnit.MINUTES);
                if (Boolean.TRUE.equals(result)) {
                    extendedSeats.add(seatId);
                }
            }
        }

        return extendedSeats;
    }


}
