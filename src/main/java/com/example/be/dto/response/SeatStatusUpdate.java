package com.example.be.dto.response;

public class SeatStatusUpdate {
    private Long seatId;
    private String seatStatus; // "SELECTED", "AVAILABLE", "BOOKED"
    private boolean temporary; // true nếu đang bị giữ

    public SeatStatusUpdate(Long seatId, String seatStatus, boolean temporary) {
        this.seatId = seatId;
        this.seatStatus = seatStatus;
        this.temporary = temporary;
    }

    // getters + setters
}
