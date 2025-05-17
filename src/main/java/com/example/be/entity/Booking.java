package com.example.be.entity;

import com.example.be.enums.SeatStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "booking")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id", referencedColumnName = "showtimeId")
    private ShowTime showtime; // Liên kết với lịch chiếu

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user; // Liên kết với người dùng đã đặt vé

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "booking_seats",
//            joinColumns = @JoinColumn(name = "booking_id"),
//            inverseJoinColumns = @JoinColumn(name = "seat_id")
//    )
//    private Set<Seat> seats; // Các ghế đã chọn trong đặt vé

    private LocalDateTime bookingTime; // Thời gian đặt vé

    @Column(name = "showtime_id", nullable = false, insertable = false, updatable = false)
    private Long showTimeId;

    @Column(name = "seat_id", nullable = false)
    private Long seatId;

    @Column(name = "user_id", nullable = false, insertable = false, updatable = false)
    private Long userId;
    @Enumerated(EnumType.STRING)
    @Column(name = "seat_status", nullable = false)
    private SeatStatus seatStatus;

//    @Column(name = "selection_time", nullable = false)
//    private LocalDateTime selectionTime;


}
