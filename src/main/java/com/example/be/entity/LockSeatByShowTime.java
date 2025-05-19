package com.example.be.entity;
import com.example.be.enums.SeatStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "lockseatbyshowtime")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class LockSeatByShowTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lockSeatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id", nullable = false)
    private ShowTime showtime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus status;
}
