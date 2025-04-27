package com.example.be.entity;
import com.example.be.enums.SeatType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "seat_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SeatInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SeatType name;

    private BigDecimal price; // Giá của loại ghế

    @OneToMany(mappedBy = "seatInfo")
    @JsonIgnore
    private List<Seat> seats; // Quan hệ 1-n với Seat
}
