package com.example.be.dto.response;

import com.example.be.enums.SeatStatus;
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
    private String seatInfoName;
    private Long seatInfoId;
}
