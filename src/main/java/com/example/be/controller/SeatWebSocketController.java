package com.example.be.controller;

import com.example.be.dto.request.SeatLockRequest;
import com.example.be.dto.request.SeatSelectionRequestDTO;
import com.example.be.dto.request.UnlockSeatRequest;
import com.example.be.dto.response.SeatStatusResponse;
import com.example.be.enums.SeatStatus;
import com.example.be.service.SeatLockService;
import com.example.be.service.SeatSelectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class SeatWebSocketController {

    private final SeatLockService seatLockService;
    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    SeatSelectionService seatSelectionService;

    // WebSocket để xử lý ghế được chọn
    @MessageMapping("/seat/select")
    public void selectSeat(SeatSelectionRequestDTO request) {
        // Kiểm tra xem ghế đã được khóa chưa
        if (seatLockService.isSeatLocked(request.getShowtimeId(), request.getSeatId())) {
            // Gửi thông báo về phía client nếu ghế đã bị khóa
            messagingTemplate.convertAndSend("/topic/seats/" + request.getShowtimeId(),
                    new SeatStatusResponse("Ghế đã bị chọn", request.getSeatId(), SeatStatus.SELECTED));
        } else {
            // Khóa ghế và lưu thông tin lên cơ sở dữ liệu
            boolean locked = seatLockService.lockSeat(request.getShowtimeId(), request.getSeatId(), request.getUserId());
            if (locked) {
                messagingTemplate.convertAndSend("/topic/seats/" + request.getShowtimeId(),
                        new SeatStatusResponse("Ghế đã được chọn", request.getSeatId(), SeatStatus.SELECTED));
            } else {
                messagingTemplate.convertAndSend("/topic/seats/" + request.getShowtimeId(),
                        new SeatStatusResponse("Không thể chọn ghế", request.getSeatId(), SeatStatus.AVAILABLE));
            }
        }
    }

    @MessageMapping("/seat/release")
    public void releaseSeat(UnlockSeatRequest seatLockRequest) {
        // Kiểm tra nếu ghế này đang được chọn
        seatSelectionService.deleteSeatSelection(seatLockRequest.getUserId(),seatLockRequest.getShowtimeId(),seatLockRequest.getSeatId(),seatLockRequest.getSeatStatus());

        if (seatLockService.isSeatLocked(seatLockRequest.getShowtimeId(), seatLockRequest.getSeatId())) {
            // Xóa dữ liệu trong bảng seat_selection

            // Gửi thông báo về phía client
            messagingTemplate.convertAndSend("/topic/seats/" + seatLockRequest.getShowtimeId(),
                    new SeatStatusResponse("Ghế đã được hủy chọn", seatLockRequest.getSeatId(), SeatStatus.AVAILABLE));
        } else {
            messagingTemplate.convertAndSend("/topic/seats/" + seatLockRequest.getShowtimeId(),
                    new SeatStatusResponse("Ghế chưa được chọn", seatLockRequest.getSeatId(), SeatStatus.AVAILABLE));
        }
    }
    @PostMapping("/seats/unlock")
    public ResponseEntity<?> unlockSeat(@RequestBody UnlockSeatRequest request) {
        seatSelectionService.deleteSeatSelection(request.getUserId(), request.getShowtimeId(), request.getSeatId(), request.getSeatStatus());
        return ResponseEntity.ok().body("Unlocked seat successfully!");
    }
}
