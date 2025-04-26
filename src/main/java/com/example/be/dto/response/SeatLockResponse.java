package com.example.be.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeatLockResponse {
    private String action; // ví dụ: "LOCKED"
    private Long seatId;
    private Long showtimeId;
    private Long userId;
}
