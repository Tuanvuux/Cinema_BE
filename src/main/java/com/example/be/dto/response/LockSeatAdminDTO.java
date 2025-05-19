package com.example.be.dto.response;

import com.example.be.enums.SeatStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LockSeatAdminDTO {
    private Long lockSeatId;
    private Long roomId;
    private String roomName;
    private Long seatId;
    private String seatName;
    private Long showtimeId;
    private SeatStatus status;
}
