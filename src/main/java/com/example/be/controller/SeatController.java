package com.example.be.controller;

import com.example.be.dto.request.SeatReleaseRequest;
import com.example.be.dto.request.SeatSelectionRequest;
import com.example.be.entity.Room;
import com.example.be.entity.Seat;
import com.example.be.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seats")
public class SeatController {
    @Autowired
    private final SeatService seatService;
    @Autowired
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
    @GetMapping("/{roomId}")
    public List<Seat> getSeatsByRoomId(@PathVariable Long roomId) {
        return seatService.getSeatsByRoomId(roomId);
    }

}
