package com.example.be.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeatAction {
    private Long seatId;
    private Long showtimeId;
    private String seatStatus; // "SELECT" hoặc "RELEASE"
    private Long userId;

    // getters + setters
}
