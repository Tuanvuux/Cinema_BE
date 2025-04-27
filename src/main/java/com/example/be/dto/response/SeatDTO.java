package com.example.be.dto.response;

import com.example.be.entity.SeatInfo;
import com.example.be.enums.SeatStatus;
import com.example.be.enums.SeatType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatDTO {
    private Long seatId;
    private String seatName;
    private Long roomId;
    private String roomName;
    private String rowLabel;
    private int columnNumber;
    private SeatStatus status;
    private SeatInfo seatInfo;
}
