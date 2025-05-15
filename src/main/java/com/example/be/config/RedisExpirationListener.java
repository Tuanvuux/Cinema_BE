package com.example.be.config;

import com.example.be.service.SeatLockService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class RedisExpirationListener implements MessageListener {

    private final SeatLockService seatLockService;

    // Constructor nhận SeatLockService
    public RedisExpirationListener(SeatLockService seatLockService) {
        this.seatLockService = seatLockService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = new String(message.getBody());

        // Chỉ quan tâm tới các key khóa ghế
        if (expiredKey.startsWith("seat-lock:")) {
            String[] parts = expiredKey.split(":");
            if (parts.length == 3) {
                try {
                    Long showtimeId = Long.parseLong(parts[1]);
                    Long seatId = Long.parseLong(parts[2]);

                    // Hủy ghế khi khóa hết hạn
                    seatLockService.unlockSeat(showtimeId, seatId);
                    System.out.println("Auto-unlocked seat due to TTL: " + expiredKey);

                } catch (NumberFormatException e) {
                    System.err.println("Failed to parse seat lock key: " + expiredKey);
                }
            }
        }
    }
}
