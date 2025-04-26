package com.example.be.controller;

import com.example.be.dto.request.SeatReleaseRequest;
import com.example.be.dto.request.SeatSelectionRequest;
import com.example.be.dto.response.SeatDTO;
import com.example.be.dto.response.SeatWithLockResponse;
import com.example.be.entity.Room;
import com.example.be.entity.Seat;
import com.example.be.service.RoomService;
import com.example.be.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seats")
@CrossOrigin(origins = "http://localhost:5173")
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

//    @GetMapping("/all")
//    public List<Seat> getAllSeats() {
//        return seatService.getSeats();
//    }

    @PostMapping("/all")
    public Seat addSeat(@RequestBody Seat seat) {
        return seatService.addSeat(seat);
    }
    @GetMapping("/admin")
    public List<SeatDTO> getSeatsWithRoomInfo() {
        return seatService.getSeatsWithRoomInfo();
    }

    @GetMapping("/{id}")
    public Seat getSeatById(@PathVariable Long id) {
        return seatService.getSeatById(id);
    }

    @DeleteMapping("/admin/{id}")
    public void deleteSeat(@PathVariable Long id) {
        seatService.deleteSeat(id);
    }

    @PostMapping("/admin")
    public SeatDTO addSeat(@RequestBody SeatDTO seatDTO) {
        return seatService.addSeatDTO(seatDTO);
    }

    @PutMapping("/admin/{id}")
    public SeatDTO updateSeat(@PathVariable Long id, @RequestBody SeatDTO seatDTO) {
        return seatService.updateSeat(id, seatDTO);
    }
    @GetMapping("/with-lock/{showtimeId}")
    public ResponseEntity<List<SeatWithLockResponse>> getSeatsWithLock(@PathVariable Long showtimeId) {
        List<SeatWithLockResponse> seats = seatService.getSeatsWithLock(showtimeId);
        return ResponseEntity.ok(seats);
    }
}
