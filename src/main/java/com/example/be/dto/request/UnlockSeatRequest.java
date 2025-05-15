package com.example.be.dto.request;

import com.example.be.enums.SeatStatus;
import lombok.Data;

@Data
public class UnlockSeatRequest {
    private Long userId;
    private Long showtimeId;
    private Long seatId;
    private SeatStatus seatStatus;
}