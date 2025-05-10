package com.example.be.repository;

import com.example.be.entity.PaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Long> {

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.paymentHistory.paymentId = :paymentId")
    List<PaymentDetail> findByPaymentHistoryId(@Param("paymentId") Long paymentId);
}
