package com.example.be.config;

import com.example.be.service.SeatLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RedisExpirationListener implements MessageListener {

    private final SeatLockService seatLockService;
    private final SimpMessagingTemplate messagingTemplate;

    // Constructor nhận các dependency
    @Autowired
    public RedisExpirationListener(SeatLockService seatLockService, SimpMessagingTemplate messagingTemplate) {
        this.seatLockService = seatLockService;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = new String(message.getBody());

        if (expiredKey.startsWith("seat:")) {
            String[] parts = expiredKey.split(":");
            if (parts.length == 3) {
                try {
                    Long showtimeId = Long.parseLong(parts[1]);
                    Long seatId = Long.parseLong(parts[2]);

                    // Chỉ gọi unlock, không gửi WebSocket ở đây
                    seatLockService.unlockSeat(showtimeId, seatId);

                    System.out.println("TTL expired, tried unlocking seat: " + expiredKey);

                } catch (NumberFormatException e) {
                    System.err.println("Failed to parse key: " + expiredKey);
                }
            }
        }
    }
}
