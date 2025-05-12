package com.example.be.service;

import com.example.be.dto.MovieDetailReportDTO;
import com.example.be.dto.MovieRevenueReportDTO;
import com.example.be.dto.MovieViewsReportDTO;
import com.example.be.dto.response.PaymentDetailDTO;
import com.example.be.dto.response.PaymentResponseDTO;
import com.example.be.entity.Movie;
import com.example.be.entity.PaymentHistory;
import com.example.be.entity.PaymentDetail;
import com.example.be.entity.ShowTime;
import com.example.be.exception.ResourceNotFoundException;
import com.example.be.repository.MovieRepository;
import com.example.be.repository.PaymentDetailRepository;
import com.example.be.repository.PaymentHistoryRepository;
import com.example.be.repository.ShowTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;
    @Autowired
    private PaymentDetailRepository paymentDetailRepository;
    @Autowired
    private ShowTimeRepository showTimeRepository;
    @Autowired
    private MovieRepository movieRepository;

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

    public List<MovieRevenueReportDTO> getMovieRevenueReport(LocalDate startDate, LocalDate endDate) {
        List<PaymentHistory> payments = getPaymentsInDateRange(startDate, endDate);
        Map<Movie, BigDecimal> movieRevenueMap = new HashMap<>();

        // Calculate revenue for each movie
        for (PaymentHistory payment : payments) {
            ShowTime showTime = payment.getBooking().getShowtime();
            Movie movie = showTime.getMovie();

            BigDecimal paymentAmount = payment.getSumPrice();
            movieRevenueMap.put(movie, movieRevenueMap.getOrDefault(movie, BigDecimal.ZERO).add(paymentAmount));
        }

        // Convert to DTOs
        return movieRevenueMap.entrySet().stream()
                .map(entry -> new MovieRevenueReportDTO(
                        entry.getKey().getMovieId(),
                        entry.getKey().getName(),
                        entry.getValue()))
                .collect(Collectors.toList());
    }

    public List<MovieViewsReportDTO> getMovieViewsReport(LocalDate startDate, LocalDate endDate) {
        List<PaymentHistory> payments = getPaymentsInDateRange(startDate, endDate);
        Map<Movie, Integer> movieTicketCountMap = new HashMap<>();

        // Calculate ticket count for each movie
        for (PaymentHistory payment : payments) {
            ShowTime showTime = payment.getBooking().getShowtime();
            Movie movie = showTime.getMovie();

            Integer ticketCount = payment.getSumTicket();
            movieTicketCountMap.put(movie, movieTicketCountMap.getOrDefault(movie, 0) + ticketCount);
        }

        // Convert to DTOs
        return movieTicketCountMap.entrySet().stream()
                .map(entry -> new MovieViewsReportDTO(
                        entry.getKey().getMovieId(),
                        entry.getKey().getName(),
                        entry.getValue()))
                .collect(Collectors.toList());
    }

    public List<MovieDetailReportDTO> getMovieDetailReport(LocalDate startDate, LocalDate endDate) {
        List<PaymentHistory> payments = getPaymentsInDateRange(startDate, endDate);
        List<ShowTime> showTimes = getShowTimesInDateRange(startDate, endDate);

        Map<Movie, MovieDetailStats> movieStatsMap = new HashMap<>();

        // Pre-populate map with all movies that had showtimes in the date range
        for (ShowTime showTime : showTimes) {
            Movie movie = showTime.getMovie();
            if (!movieStatsMap.containsKey(movie)) {
                movieStatsMap.put(movie, new MovieDetailStats());
            }

            // Increment showtime count
            movieStatsMap.get(movie).incrementShowtimeCount();

            // Add room capacity for calculating occupancy rate later
            movieStatsMap.get(movie).addRoomCapacity(calculateRoomCapacity(showTime.getRoom().getId()));
        }

        // Process payments
        for (PaymentHistory payment : payments) {
            ShowTime showTime = payment.getBooking().getShowtime();
            Movie movie = showTime.getMovie();

            // Ensure the movie exists in our map
            if (!movieStatsMap.containsKey(movie)) {
                movieStatsMap.put(movie, new MovieDetailStats());
            }

            // Update stats
            MovieDetailStats stats = movieStatsMap.get(movie);
            stats.addRevenue(payment.getSumPrice());
            stats.addTicketCount(payment.getSumTicket());
        }

        // Convert to DTOs
        return movieStatsMap.entrySet().stream()
                .map(entry -> {
                    MovieDetailStats stats = entry.getValue();
                    Movie movie = entry.getKey();

                    // Calculate occupancy rate
                    int occupancyRate = 0;
                    if (stats.getTotalCapacity() > 0) {
                        occupancyRate = (int) ((double) stats.getTicketCount() / stats.getTotalCapacity() * 100);
                    }

                    return new MovieDetailReportDTO(
                            movie.getMovieId(),
                            movie.getName(),
                            stats.getRevenue(),
                            stats.getTicketCount(),
                            stats.getShowtimeCount(),
                            occupancyRate);
                })
                .sorted(Comparator.comparing(MovieDetailReportDTO::getRevenue).reversed())
                .collect(Collectors.toList());
    }

    // Helper methods
    private List<PaymentHistory> getPaymentsInDateRange(LocalDate startDate, LocalDate endDate) {
        // Implementation depends on your repository methods
        return paymentHistoryRepository.findByDateTransactionBetween(
                startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay());
    }

    private List<ShowTime> getShowTimesInDateRange(LocalDate startDate, LocalDate endDate) {
        // Implementation depends on your repository methods
        return showTimeRepository.findByShowDateBetween(startDate, endDate);
    }

    private int calculateRoomCapacity(Long roomId) {
        // You would typically get this from your database
        // This is a placeholder implementation
        return 100; // Assuming average room capacity of 100 seats
    }

    // Helper class for accumulating stats
    private static class MovieDetailStats {
        private BigDecimal revenue = BigDecimal.ZERO;
        private int ticketCount = 0;
        private int showtimeCount = 0;
        private int totalCapacity = 0;

        public void addRevenue(BigDecimal amount) {
            if (amount != null) {
                revenue = revenue.add(amount);
            }
        }

        public void addTicketCount(Integer count) {
            if (count != null) {
                ticketCount += count;
            }
        }

        public void incrementShowtimeCount() {
            showtimeCount++;
        }

        public void addRoomCapacity(int capacity) {
            totalCapacity += capacity;
        }

        public BigDecimal getRevenue() {
            return revenue;
        }

        public int getTicketCount() {
            return ticketCount;
        }

        public int getShowtimeCount() {
            return showtimeCount;
        }

        public int getTotalCapacity() {
            return totalCapacity;
        }
    }
}