package com.example.be.dto.request;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeatLockRequest {
    private Long seatId;
    private Long showtimeId;
    private Long userId;
}
