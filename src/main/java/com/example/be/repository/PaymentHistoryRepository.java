package com.example.be.repository;

import com.example.be.entity.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {
    // Find payments by date range
    @Query("SELECT p FROM PaymentHistory p WHERE p.DateTransaction BETWEEN :startDate AND :endDate")
    List<PaymentHistory> findByDateTransactionBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    // Additional methods for filtering if needed
    List<PaymentHistory> findByUserUserId(Long userId);

    List<PaymentHistory> findByBookingBookingId(Long bookingId);
}
