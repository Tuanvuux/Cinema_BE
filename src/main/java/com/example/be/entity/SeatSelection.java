//package com.example.be.entity;
//
//import com.example.be.enums.SeatStatus;
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "seat_selection")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@ToString
//public class SeatSelection {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long selectionId;
//
//    @Column(name = "showtime_id", nullable = false)
//    private Long showTimeId;
//
//    @Column(name = "seat_id", nullable = false)
//    private Long seatId;
//
//    @Column(name = "user_id", nullable = false)
//    private Long userId;
//    @Enumerated(EnumType.STRING)
//    @Column(name = "seat_status", nullable = false)
//    private SeatStatus seatStatus;
//
//    @Column(name = "selection_time", nullable = false)
//    private LocalDateTime selectionTime;
//}
