package com.example.be.service;

import com.example.be.entity.SeatSelection;
import com.example.be.enums.SeatStatus;
import com.example.be.repository.SeatSelectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SeatSelectionService {

    private final SeatSelectionRepository seatSelectionRepository;

    public SeatSelectionService(SeatSelectionRepository seatSelectionRepository) {
        this.seatSelectionRepository = seatSelectionRepository;
    }

    @Transactional
    public void releaseExpiredSelections() {
        LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(5);
        List<SeatSelection> expiredSelections = seatSelectionRepository.findAllBySeatStatusAndSelectionTimeBefore(SeatStatus.SELECTED, expirationTime);

        for (SeatSelection selection : expiredSelections) {
            seatSelectionRepository.delete(selection);
            // (optional) gửi thông báo websocket nếu có
        }
    }

    // Phương thức để lưu ghế khi người dùng chọn
    @Transactional
    public boolean lockSeat(Long showtimeId, Long seatId, Long userId) {
        SeatSelection seatSelection = new SeatSelection();
        seatSelection.setShowTimeId(showtimeId);
        seatSelection.setSeatId(seatId);
        seatSelection.setUserId(userId);
        seatSelection.setSeatStatus("SELECTED");  // Đánh dấu ghế đã được chọn
        seatSelection.setSelectionTime(LocalDateTime.now());

        // Lưu vào cơ sở dữ liệu
        seatSelectionRepository.save(seatSelection);
        return true;
    }

    // Phương thức để hủy chọn ghế
    @Transactional
    public void unlockSeat(Long showtimeId, Long seatId) {
        SeatSelection seatSelection = seatSelectionRepository.findByShowTimeIdAndSeatId(showtimeId, seatId);
        if (seatSelection != null) {
            seatSelectionRepository.delete(seatSelection);
        }
    }
    @Transactional
    public void deleteSeatSelection(Long userId, Long showTimeId, Long seatId, String seatStatus) {
        seatSelectionRepository.deleteByUserIdAndShowTimeIdAndSeatIdAndSeatStatus(userId, showTimeId, seatId, seatStatus);
    }
}
