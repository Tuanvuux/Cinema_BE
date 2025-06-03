package com.example.be.controller;

import com.example.be.dto.MovieDetailReportDTO;
import com.example.be.dto.MovieRevenueReportDTO;
import com.example.be.dto.MovieViewsReportDTO;
import com.example.be.dto.request.PaymentHistoryRequestDTO;
import com.example.be.dto.response.PaymentDTOResponse;
import com.example.be.dto.response.PaymentHistoryDTO;
import com.example.be.dto.response.PaymentResponseDTO;
import com.example.be.entity.PaymentHistory;
import com.example.be.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/payments")
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/admin")
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {
        List<PaymentResponseDTO> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/admin/by-date-range")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        System.out.println("Received startDate: " + startDate);
        System.out.println("Received endDate: " + endDate);

        List<PaymentResponseDTO> payments = paymentService.getPaymentsByDateRange(startDate, endDate);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/admin/{paymentId}/details")
    public ResponseEntity<PaymentResponseDTO> getPaymentDetails(@PathVariable Long paymentId) {
        PaymentResponseDTO payment = paymentService.getPaymentDetails(paymentId);
        return ResponseEntity.ok(payment);
    }


    @GetMapping("/admin/movies-revenue")
    public ResponseEntity<List<MovieRevenueReportDTO>> getMovieRevenueReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return ResponseEntity.ok(paymentService.getMovieRevenueReport(startDate, endDate));
    }

    @GetMapping("/admin/movies-views")
    public ResponseEntity<List<MovieViewsReportDTO>> getMovieViewsReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return ResponseEntity.ok(paymentService.getMovieViewsReport(startDate, endDate));
    }

    @GetMapping("/admin/movies-details")
    public ResponseEntity<List<MovieDetailReportDTO>> getMovieDetailReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return ResponseEntity.ok(paymentService.getMovieDetailReport(startDate, endDate));
    }
    @PostMapping("/addPayment")
    public ResponseEntity<PaymentDTOResponse> createPayment(@RequestBody PaymentHistoryRequestDTO dto) {
        PaymentHistory newPaymentHistory = paymentService.createPayment(dto);
        PaymentDTOResponse paymentDTOResponse = paymentService.convertPaymentDTO(newPaymentHistory);
        return ResponseEntity.ok(paymentDTOResponse);
    }
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentHistoryDTO> getPaymentHistoryById(@PathVariable Long paymentId) {
        PaymentHistoryDTO dto = paymentService.getPaymentHistoryById(paymentId);
        return ResponseEntity.ok(dto);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentHistoryDTO>> getPaymentHistoryByUserId(@PathVariable long userId) {
        List<PaymentHistoryDTO> paymentHistory = paymentService.getSuccessfulPaymentHistoryByUserId(userId);
        return ResponseEntity.ok(paymentHistory);
    }
}
