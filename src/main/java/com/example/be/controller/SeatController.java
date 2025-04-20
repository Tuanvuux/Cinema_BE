package com.example.be.controller;

import com.example.be.dto.request.SeatReleaseRequest;
import com.example.be.dto.request.SeatSelectionRequest;
import com.example.be.service.SeatService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seats")
public class SeatController {

    private final SeatService seatService;
    private final SimpMessagingTemplate messagingTemplate;

    public SeatController(SeatService seatService, SimpMessagingTemplate messagingTemplate) {
        this.seatService = seatService;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/select")
    public void selectSeat(@RequestBody SeatSelectionRequest request) {
        seatService.selectSeat(request.getShowtimeId(), request.getSeatId());

        // Gửi thông báo cho tất cả các client về sự thay đổi trạng thái ghế
        messagingTemplate.convertAndSend("/topic/seats/" + request.getShowtimeId(),
                "Seat " + request.getSeatId() + " selected.");
    }

    @PostMapping("/release")
    public void releaseSeat(@RequestBody SeatReleaseRequest request) {
        seatService.releaseSeat(request.getShowtimeId(), request.getSeatId());

        // Gửi thông báo cho tất cả các client về sự thay đổi trạng thái ghế
        messagingTemplate.convertAndSend("/topic/seats/" + request.getShowtimeId(),
                "Seat " + request.getSeatId() + " released.");
    }
}
