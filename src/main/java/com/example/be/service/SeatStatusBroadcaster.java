package com.example.be.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SeatStatusBroadcaster {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void broadcastSeatBooked(Long showtimeId, List<Long> bookedSeatIds) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("showtimeId", showtimeId);
        payload.put("seatIds", bookedSeatIds);
        payload.put("status", "BOOKED");

        messagingTemplate.convertAndSend("/topic/seats/" + showtimeId, payload);
    }
}
