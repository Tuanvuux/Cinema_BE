package com.example.be.dto.response;

import com.example.be.enums.SeatStatus;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SeatStatusResponse {
    private String message;
    private Long seatId;
    private SeatStatus status;
    @Nullable
    private Long userId;
}