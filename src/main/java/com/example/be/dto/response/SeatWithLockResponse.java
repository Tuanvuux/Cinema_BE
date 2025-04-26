package com.example.be.dto.response;


import com.example.be.enums.SeatStatus;
import com.example.be.enums.SeatType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatWithLockResponse {
    private Long seatId;
    private String seatName;
    private String rowLabel;
    private int columnNumber;
    private SeatStatus status;
    private SeatType seatType;
    private boolean isLocked; // TRUE nếu ghế đang bị khóa (qua Redis)
}