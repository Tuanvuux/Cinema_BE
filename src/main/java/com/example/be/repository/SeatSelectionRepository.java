//package com.example.be.repository;
//
//import com.example.be.entity.SeatSelection;
//import com.example.be.enums.SeatStatus;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//public interface SeatSelectionRepository extends JpaRepository<SeatSelection, Integer> {
//    List<SeatSelection> findAllBySeatStatusAndSelectionTimeBefore(SeatStatus seatStatus, LocalDateTime time);
//    boolean existsByShowTimeIdAndSeatIdAndSeatStatus(Long showtimeId, Long seatId, SeatStatus seatStatus);
//    SeatSelection findByShowTimeIdAndSeatId(Long showtimeId, Long seatId);
//    void deleteByUserIdAndShowTimeIdAndSeatIdAndSeatStatus(Long userId, Long showTimeId, Long seatId, SeatStatus seatStatus);
//    List<SeatSelection> findByShowTimeId(Long showTimeId);
//    List<SeatSelection> findByShowTimeIdAndSeatStatus(Long showTimeId, SeatStatus seatStatus);
//
//}
