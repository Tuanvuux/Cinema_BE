package com.example.be.repository;

import com.example.be.entity.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {
    // Find payments by date range
    @Query("SELECT p FROM PaymentHistory p WHERE p.DateTransaction BETWEEN :startDate AND :endDate AND p.Status = 'SUCCESS'")
    List<PaymentHistory> findByDateTransactionBetweenAndStatus(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
    // Additional methods for filtering if needed
    List<PaymentHistory> findByUserUserId(Long userId);
    Optional<PaymentHistory> findByPaymentId(Long paymentId);
    @Query("SELECT p FROM PaymentHistory p WHERE p.Status IS NOT NULL")
    List<PaymentHistory> findAllWithNonNullStatus();
    @Query("""
    SELECT DISTINCT ph FROM PaymentHistory ph
    JOIN FETCH ph.showTime st
    JOIN FETCH st.movie
    JOIN FETCH st.room
    JOIN FETCH ph.paymentDetails pd
    JOIN FETCH pd.seat
    WHERE ph.user.userId = :userId
      AND ph.Status = 'SUCCESS'
""")
    List<PaymentHistory> findSuccessfulPaymentsByUserId(@Param("userId") Long userId);
}