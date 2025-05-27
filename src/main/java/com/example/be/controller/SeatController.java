package com.example.be.controller;

import com.example.be.dto.request.SeatReleaseRequest;
import com.example.be.dto.request.SeatSelectionRequest;
import com.example.be.dto.response.SeatDTO;
import com.example.be.dto.response.SeatWithLockResponse;
import com.example.be.entity.LockSeatByShowTime;
import com.example.be.entity.Room;
import com.example.be.entity.Seat;
import com.example.be.entity.SeatInfo;
import com.example.be.service.RoomService;
import com.example.be.service.SeatService;
import com.example.be.service.ShowTimeService;
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
    @Autowired
    private ShowTimeService showTimeService;

    public SeatController(SeatService seatService, SimpMessagingTemplate messagingTemplate) {
        this.seatService = seatService;
        this.messagingTemplate = messagingTemplate;
    }
    @GetMapping("/{roomId}")
    public List<Seat> getSeatsByRoomId(@PathVariable Long roomId) {
        return seatService.getSeatsByRoomId(roomId);
    }

    @PostMapping("/all")
    public Seat addSeat(@RequestBody Seat seat) {
        return seatService.addSeat(seat);
    }
    @GetMapping("/admin")
    public List<SeatDTO> getSeatsWithRoomInfo() {
        return seatService.getSeatsWithRoomInfo();
    }

    @GetMapping("/admin/seatinfo")
    public List<SeatInfo> seatinfo() {
        return seatService.getSeatInfo();
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
    @GetMapping("/admin/countseat")
    public long countSeats() {
        return seatService.countSeat();
    }
    @GetMapping("/maintenance/{showtimeId}")
    public List<Long> getMaintenanceSeats(@PathVariable long showtimeId){
        return seatService.getMaintenanceSeats(showtimeId);
    }

    @GetMapping("/admin/check-exist/{seatName}/{roomId}")
    public ResponseEntity<?> checkSeatExists(
            @PathVariable String seatName,
            @PathVariable Long roomId
    ) {
        boolean exists = seatService.isSeatExistsInRoom(seatName, roomId);
        return ResponseEntity.ok().body(exists);
    }

}
