//package com.example.be.service;
//
//import com.example.be.entity.SeatSelection;
//import com.example.be.enums.SeatStatus;
//import com.example.be.repository.SeatSelectionRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Service
//public class SeatSelectionService {
//
//    private final SeatSelectionRepository seatSelectionRepository;
//    @Autowired
//    private RedisTemplate<String, String> redisTemplate;
//
//
//    public SeatSelectionService(SeatSelectionRepository seatSelectionRepository) {
//        this.seatSelectionRepository = seatSelectionRepository;
//    }
//
//    @Transactional
//    public void releaseExpiredSelections() {
//        LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(5);
//        List<SeatSelection> expiredSelections = seatSelectionRepository.findAllBySeatStatusAndSelectionTimeBefore(SeatStatus.SELECTED, expirationTime);
//
//        for (SeatSelection selection : expiredSelections) {
//            seatSelectionRepository.delete(selection);
//            // (optional) gửi thông báo websocket nếu có
//        }
//    }
//
//    // Phương thức để lưu ghế khi người dùng chọn
//    @Transactional
//    public boolean lockSeat(Long showtimeId, Long seatId, Long userId) {
//        SeatSelection seatSelection = new SeatSelection();
//        seatSelection.setShowTimeId(showtimeId);
//        seatSelection.setSeatId(seatId);
//        seatSelection.setUserId(userId);
//        seatSelection.setSeatStatus(SeatStatus.SELECTED);  // Đánh dấu ghế đã được chọn
//        seatSelection.setSelectionTime(LocalDateTime.now());
//
//        // Lưu vào cơ sở dữ liệu
//        seatSelectionRepository.save(seatSelection);
//        return true;
//    }
//
//    // Phương thức để hủy chọn ghế
//    @Transactional
//    public void unlockSeat(Long showtimeId, Long seatId) {
//        SeatSelection seatSelection = seatSelectionRepository.findByShowTimeIdAndSeatId(showtimeId, seatId);
//        if (seatSelection != null) {
//            seatSelectionRepository.delete(seatSelection);
//        }
//    }
//    @Transactional
//    public void deleteSeatSelection(Long userId, Long showTimeId, Long seatId, SeatStatus seatStatus) {
//        seatSelectionRepository.deleteByUserIdAndShowTimeIdAndSeatIdAndSeatStatus(userId, showTimeId, seatId, seatStatus);
//    }
//    public List<Long> getBookedSeatIds(Long showTimeId) {
//        List<SeatSelection> bookedSeats = seatSelectionRepository.findByShowTimeIdAndSeatStatus(showTimeId, SeatStatus.BOOKED);
//        return bookedSeats.stream()
//                .map(SeatSelection::getSeatId)
//                .collect(Collectors.toList());
//    }
//    public void updateSeatStatus(String showtimeId, String seatId, String status, String userId) {
//        String key = "seat:" + showtimeId + ":" + seatId;
//        Map<String, String> seatData = new HashMap<>();
//        seatData.put("status", status);
//        seatData.put("userId", userId);
//        seatData.put("timestamp", String.valueOf(System.currentTimeMillis()));
//
//        redisTemplate.opsForHash().putAll(key, seatData);
//        redisTemplate.expire(key, Duration.ofMinutes(30)); // TTL cho ghế đã chọn
//    }
//
//    public Map<String, String> getSeatStatusMap(String showtimeId, String seatId) {
//        String key = "seat:" + showtimeId + ":" + seatId;
//        return redisTemplate.<String, String>opsForHash().entries(key);
//    }
//}
