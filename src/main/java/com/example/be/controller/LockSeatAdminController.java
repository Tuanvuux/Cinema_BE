package com.example.be.controller;

import com.example.be.dto.response.LockSeatAdminDTO;
import com.example.be.dto.response.SeatDTO;
import com.example.be.entity.LockSeatByShowTime;
import com.example.be.entity.Seat;
import com.example.be.entity.ShowTime;
import com.example.be.service.SeatLockAdminService;
import com.example.be.service.SeatService;
import com.example.be.service.ShowTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lock-seat-admin")
@CrossOrigin(origins = "http://localhost:5173")
public class LockSeatAdminController {
    @Autowired
    private SeatLockAdminService seatLockAdminService;
    @Autowired
    private ShowTimeService showTimeService;


    @GetMapping("/admin")
    public ResponseEntity<List<LockSeatAdminDTO>> getAllLockSeatAdmin(){
        List<LockSeatByShowTime> locks = seatLockAdminService.getLockSeatAdmin();
        List<LockSeatAdminDTO> responses = locks.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    private LockSeatAdminDTO mapToResponse(LockSeatByShowTime lock) {
        return new LockSeatAdminDTO(
                lock.getLockSeatId(),
                lock.getRoom().getId(),
                lock.getRoom().getName(),
                lock.getSeat().getSeatId(),
                lock.getSeat().getSeatName(),
                lock.getShowtime().getShowtimeId(),
                lock.getStatus()
        );
    }

    @DeleteMapping("/admin/{id}")
    public void deleteLockSeat(@PathVariable Long id) {
        seatLockAdminService.deleteLockSeat(id);
    }

    @GetMapping("/admin/getShowTimeByRoom/{roomId}")
    public List<ShowTime> getShowTimeByRoom(@PathVariable Long roomId){
        return showTimeService.getShowtimesByRoom(roomId);
    }

    @GetMapping("/admin/getSeatByShowTime/{id}")
    public ResponseEntity<List<Seat>> getSeatsByShowtime(@PathVariable Long id) {
        try {
            List<Seat> seats = seatLockAdminService.getSeatsByShowtimeId(id);
            if (seats.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(seats, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/admin")
    public LockSeatAdminDTO addLockSeatAdmin(@RequestBody LockSeatAdminDTO seatDTO) {
        return seatLockAdminService.addLockSeatAdminDTO(seatDTO);
    }
}
