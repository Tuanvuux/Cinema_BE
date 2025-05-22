package com.example.be.controller;

import com.example.be.dto.request.BookingRequestDTO;
import com.example.be.dto.request.ExtendLockRequest;
import com.example.be.dto.request.SeatSelectionRequestDTO;
import com.example.be.dto.request.UnlockSeatRequest;
import com.example.be.dto.response.LockedSeatDTO;
import com.example.be.dto.response.SeatStatusResponse;
import com.example.be.enums.SeatStatus;
import com.example.be.service.BookingService;
import com.example.be.service.SeatLockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SeatWebSocketController {

    private final SeatLockService seatLockService;
    private final BookingService bookingService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/seat/select")
    public void handleSeatSelection(SeatSelectionRequestDTO request) {
        Long showtimeId = request.getShowtimeId();
        Long seatId = request.getSeatId();
        Long userId = request.getUserId();

        if (seatLockService.isSeatLocked(showtimeId, seatId)) {
            messagingTemplate.convertAndSend("/topic/seats/" + showtimeId,
                    new SeatStatusResponse("Ghế đã bị giữ", seatId, SeatStatus.SELECTED,userId));
        } else {
            boolean locked = seatLockService.lockSeat(showtimeId, seatId, userId);
            SeatStatus status = locked ? SeatStatus.SELECTED : SeatStatus.AVAILABLE;
            String message = locked ? "Ghế đã được giữ" : "Không thể giữ ghế";

            messagingTemplate.convertAndSend("/topic/seats/" + showtimeId,
                    new SeatStatusResponse(message, seatId, status, userId));
        }
    }

    @MessageMapping("/seat/release")
    public void handleSeatRelease(UnlockSeatRequest request) {
        Long showtimeId = request.getShowtimeId();
        Long seatId = request.getSeatId();
        Long userId = request.getUserId();

        bookingService.deleteSeatSelection(userId, showtimeId, seatId, request.getSeatStatus());
        seatLockService.unlockSeat(showtimeId, seatId);

        messagingTemplate.convertAndSend("/topic/seats/" + showtimeId,
                new SeatStatusResponse("Ghế đã được hủy giữ", seatId, SeatStatus.AVAILABLE, userId));
    }

//    @PostMapping("/seats/unlock")
//    public ResponseEntity<String> unlockSeat(@RequestBody UnlockSeatRequest request) {
//        bookingService.deleteSeatSelection(request.getUserId(), request.getShowtimeId(), request.getSeatId(), request.getSeatStatus());
//        seatLockService.unlockSeat(request.getShowtimeId(), request.getSeatId());
//        return ResponseEntity.ok("Đã mở khóa ghế thành công!");
//    }

    @GetMapping("/booked/{showtimeId}")
    public List<Long> getBookedSeats(@PathVariable Long showtimeId) {
        return bookingService.getBookedSeatIds(showtimeId);
    }
    @GetMapping("/locked/{showtimeId}")
    public ResponseEntity<List<LockedSeatDTO>> getLockedSeats(@PathVariable Long showtimeId) {
        List<LockedSeatDTO> lockedSeats = seatLockService.getLockedSeats(showtimeId);
        return ResponseEntity.ok(lockedSeats);
    }
    @PostMapping("/booking")
    public ResponseEntity<String> book(@RequestBody BookingRequestDTO request) {
        bookingService.bookSeats(request.getUserId(), request.getShowtimeId(), request.getSeatIds());
        return ResponseEntity.ok("Đặt vé thành công");
    }
    @PostMapping("/seats/extend-lock")
    public ResponseEntity<?> extendLock(@RequestBody ExtendLockRequest request) {
        List<Long> extended = seatLockService.extendSeatLocks(
                request.getShowtimeId(),
                request.getUserId(),
                request.getSeatIds()
        );

        return ResponseEntity.ok().body("Đã gia hạn " + extended.size() + " ghế: " + extended);
    }


}
