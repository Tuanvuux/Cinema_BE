package com.example.be.service;

import org.springframework.stereotype.Service;

@Service
public class SeatService {

    public void selectSeat(String showtimeId, String seatId) {
        // Lógica chọn ghế, ví dụ: lưu trạng thái ghế vào Redis
    }

    public void releaseSeat(String showtimeId, String seatId) {
        // Lógica thả ghế, ví dụ: xóa trạng thái ghế khỏi Redis
    }
}