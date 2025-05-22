package com.example.be.service;

import com.example.be.dto.MovieDetailReportDTO;
import com.example.be.dto.MovieRevenueReportDTO;
import com.example.be.dto.MovieViewsReportDTO;
import com.example.be.dto.request.PaymentHistoryRequestDTO;
import com.example.be.dto.response.PaymentDTOResponse;
import com.example.be.dto.response.PaymentDetailDTO;
import com.example.be.dto.response.PaymentResponseDTO;
import com.example.be.entity.*;
import com.example.be.enums.SeatStatus;
import com.example.be.exception.ResourceNotFoundException;
import com.example.be.repository.*;
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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private SeatStatusBroadcaster seatStatusBroadcaster;

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
        if (paymentHistory.getShowTime() != null) {
            dto.setScheduleId(paymentHistory.getShowTime().getShowtimeId());
            dto.setMovieName(paymentHistory.getShowTime().getMovie().getName());
            dto.setRoomName(paymentHistory.getShowTime().getRoom().getName());
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
            ShowTime showTime = payment.getShowTime();
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
            ShowTime showTime = payment.getShowTime();
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

//    public List<MovieDetailReportDTO> getMovieDetailReport(LocalDate startDate, LocalDate endDate) {
//        List<PaymentHistory> payments = getPaymentsInDateRange(startDate, endDate);
//        List<ShowTime> showTimes = getShowTimesInDateRange(startDate, endDate);
//
//        Map<Movie, MovieDetailStats> movieStatsMap = new HashMap<>();
//
//        // Trước tiên xử lý tất cả các phim có payment trong khoảng thời gian
//        for (PaymentHistory payment : payments) {
//            if (payment.getShowtime() != null && payment.getShowtime().getMovie() != null) {
//                Movie movie = payment.getShowtime().getMovie();
//
//                // Đảm bảo phim tồn tại trong map
//                movieStatsMap.putIfAbsent(movie, new MovieDetailStats());
//
//                // Cập nhật doanh thu và số vé
//                MovieDetailStats stats = movieStatsMap.get(movie);
//                stats.addRevenue(payment.getSumPrice());
//                stats.addTicketCount(payment.getSumTicket());
//            }
//        }
//
//        // Sau đó xử lý showtime cho những phim đã có trong map hoặc chưa có payment
//        for (ShowTime showTime : showTimes) {
//            Movie movie = showTime.getMovie();
//
//            // Đảm bảo phim tồn tại trong map
//            movieStatsMap.putIfAbsent(movie, new MovieDetailStats());
//
//            // Cập nhật số suất chiếu
//            movieStatsMap.get(movie).incrementShowtimeCount();
//
//            // Cập nhật sức chứa phòng
//            movieStatsMap.get(movie).addRoomCapacity(calculateRoomCapacity(showTime.getRoom().getId()));
//        }
//
//        // Chuyển đổi sang DTO và trả về kết quả
//        return movieStatsMap.entrySet().stream()
//                .map(entry -> {
//                    MovieDetailStats stats = entry.getValue();
//                    Movie movie = entry.getKey();
//
//                    // Tính tỷ lệ lấp đầy
//                    int occupancyRate = 0;
//                    if (stats.getTotalCapacity() > 0) {
//                        occupancyRate = (int) ((double) stats.getTicketCount() / stats.getTotalCapacity() * 100);
//                    }
//
//                    return new MovieDetailReportDTO(
//                            movie.getMovieId(),
//                            movie.getName(),
//                            stats.getRevenue(),
//                            stats.getTicketCount(),
//                            stats.getShowtimeCount(),
//                            occupancyRate);
//                })
//                .sorted(Comparator.comparing(MovieDetailReportDTO::getRevenue).reversed())
//                .collect(Collectors.toList());
//    }

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
            ShowTime showTime = payment.getShowTime();
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
        return roomRepository.findById(roomId)
                .map(Room::getSeatCount)
                .orElse(0); // hoặc throw nếu muốn kiểm soát chặt chẽ
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
    public PaymentHistory createPayment(PaymentHistoryRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        ShowTime showTime = showTimeRepository.findById(dto.getShowtimeId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy suất chiếu"));

        PaymentHistory payment = new PaymentHistory();
        payment.setDateTransaction(dto.getDateTransaction());
        payment.setSumTicket(dto.getSumTicket());
        payment.setSumPrice(dto.getSumPrice());
        payment.setMethodPayment(dto.getMethodPayment());
        payment.setStatus(dto.getStatus());
        payment.setUser(user);
        payment.setShowTime(showTime);

        return paymentHistoryRepository.save(payment);
    }
    public PaymentDTOResponse convertPaymentDTO(PaymentHistory paymentHistory){
        PaymentDTOResponse paymentDTOResponse = new PaymentDTOResponse();
        paymentDTOResponse.setPaymentId(paymentHistory.getPaymentId());
        paymentDTOResponse.setSumPrice(paymentHistory.getSumPrice());
        return paymentDTOResponse;
    }
    public void updatePaymentStatus(String orderIdStr, String result) {
        if (!"0".equals(result)) return;

        try {
            Long paymentId = Long.parseLong(orderIdStr);
            Optional<PaymentHistory> optional = paymentHistoryRepository.findByPaymentId(paymentId);

            if (optional.isPresent()) {
                PaymentHistory payment = optional.get();

                payment.setStatus("SUCCESS");
                payment.setDateTransaction(LocalDateTime.now());
                paymentHistoryRepository.save(payment);
                savePaymentDetails(payment.getUser().getUserId(), payment.getShowTime().getShowtimeId(), payment);
                // Cập nhật ghế
                updateBookedSeats(payment.getUser().getUserId(), payment.getShowTime().getShowtimeId());

                // Lưu thông tin từng ghế vào bảng payment_detail


                System.out.println("✅ Đã cập nhật trạng thái thanh toán và lưu chi tiết ghế cho đơn hàng #" + paymentId);

            } else {
                System.out.println("⚠️ Không tìm thấy đơn hàng #" + paymentId);
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ orderId không hợp lệ: " + orderIdStr);
        }
    }

    public void updateBookedSeats(Long userId, Long showtimeId) {
        // Tìm các ghế đang được user giữ
        List<Booking> selectedSeats = bookingRepository
                .findByUserIdAndShowTimeIdAndSeatStatus(userId, showtimeId, SeatStatus.SELECTED);

        // Cập nhật trạng thái thành BOOKED
        for (Booking booking : selectedSeats) {
            booking.setSeatStatus(SeatStatus.BOOKED);
        }
        bookingRepository.saveAll(selectedSeats);

        // Gửi sự kiện WebSocket thông báo các ghế đã được BOOKED
        List<Long> bookedSeatIds = selectedSeats.stream()
                .map(Booking::getSeatId)
                .collect(Collectors.toList());

        seatStatusBroadcaster.broadcastSeatBooked(showtimeId, bookedSeatIds);

        System.out.println("🎟️ Đã cập nhật trạng thái ghế về BOOKED và gửi WebSocket cho user khác");
    }
    public void savePaymentDetails(Long userId, Long showtimeId, PaymentHistory payment) {
        List<Booking> selectedSeats = bookingRepository
                .findByUserIdAndShowTimeIdAndSeatStatus(userId, showtimeId, SeatStatus.SELECTED);

        List<PaymentDetail> details = new ArrayList<>();

        for (Booking booking : selectedSeats) {
            Optional<Seat> seatOptional = seatRepository.findById(booking.getSeatId());

            if (seatOptional.isPresent()) {
                Seat seat = seatOptional.get();

                PaymentDetail detail = new PaymentDetail();
                detail.setPaymentHistory(payment);
                detail.setSeat(seat);
                detail.setPrice(seat.getSeatInfo().getPrice()); // hoặc giá cố định nếu không có trong Seat

                details.add(detail);
            } else {
                System.err.println("⚠️ Không tìm thấy ghế với ID: " + booking.getSeatId());
            }
        }

        paymentDetailRepository.saveAll(details);
    }


}