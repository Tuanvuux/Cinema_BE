package com.example.be.service;

import com.example.be.dto.response.PaymentDetailDTO;
import com.example.be.dto.response.PaymentResponseDTO;
import com.example.be.entity.PaymentHistory;
import com.example.be.entity.PaymentDetail;
import com.example.be.exception.ResourceNotFoundException;
import com.example.be.repository.PaymentDetailRepository;
import com.example.be.repository.PaymentHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;
    @Autowired
    private PaymentDetailRepository paymentDetailRepository;

    public List<PaymentResponseDTO> getAllPayments() {
        List<PaymentHistory> paymentHistories = paymentHistoryRepository.findAll();
        return paymentHistories.stream()
                .map(this::convertToPaymentResponseDTO)
                .collect(Collectors.toList());
    }


    public List<PaymentResponseDTO> getPaymentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<PaymentHistory> paymentHistories = paymentHistoryRepository.findByDateTransactionBetween(startDate, endDate);
        return paymentHistories.stream()
                .map(this::convertToPaymentResponseDTO)
                .collect(Collectors.toList());
    }


    private PaymentResponseDTO convertToPaymentResponseDTO(PaymentHistory paymentHistory) {
        PaymentResponseDTO dto = new PaymentResponseDTO();

        // Set basic payment information
        dto.setPaymentId(paymentHistory.getPaymentId());
        dto.setDateTransaction(paymentHistory.getDateTransaction());
        dto.setSumTicket(paymentHistory.getSumTicket());
        dto.setSumPrice(paymentHistory.getSumPrice());
        dto.setMethodPayment(paymentHistory.getMethodPayment());

        // Set user information if available
        if (paymentHistory.getUser() != null) {
            dto.setUserId(paymentHistory.getUser().getUserId());
            dto.setUsername(paymentHistory.getUser().getUsername());
        }

        // Set booking information if available
        if (paymentHistory.getBooking() != null) {
            dto.setBookingId(paymentHistory.getBooking().getBookingId());

            // Set movie information if available from booking
            if (paymentHistory.getBooking().getShowtime()!= null &&
                    paymentHistory.getBooking().getShowtime().getMovie() != null) {
                dto.setMovieName(paymentHistory.getBooking().getShowtime().getMovie().getName());
                dto.setScheduleId(paymentHistory.getBooking().getShowtime().getShowtimeId());
                dto.setRoomName(paymentHistory.getBooking().getShowtime().getRoom().getName());
            }
        }

        // Set payment details information
        List<String> seatNames = new ArrayList<>();
        for (PaymentDetail detail : paymentHistory.getPaymentDetails()) {
            if (detail.getSeat() != null) {
                seatNames.add(detail.getSeat().getSeatName());
            }
        }
        dto.setSeatNames(String.join(", ", seatNames));

        return dto;
    }

    public PaymentResponseDTO getPaymentDetails(Long paymentId) {
        PaymentHistory paymentHistory = paymentHistoryRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        PaymentResponseDTO dto = convertToPaymentResponseDTO(paymentHistory);

        // Lấy thông tin chi tiết ghế
        List<PaymentDetailDTO> detailDTOs = paymentDetailRepository.findByPaymentHistoryId(paymentId)
                .stream()
                .map(this::convertToPaymentDetailDTO)
                .collect(Collectors.toList());

        dto.setPaymentDetails(detailDTOs);
        return dto;
    }

    private PaymentDetailDTO convertToPaymentDetailDTO(PaymentDetail detail) {
        return PaymentDetailDTO.builder()
                .id(detail.getId())
                .seatName(detail.getSeat().getSeatName())
                .price(detail.getPrice())
                .build();
    }
}