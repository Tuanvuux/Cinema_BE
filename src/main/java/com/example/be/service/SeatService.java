package com.example.be.service;

import com.example.be.entity.Seat;
import com.example.be.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {
    @Autowired
    SeatRepository seatRepository;

    public void selectSeat(String showtimeId, String seatId) {
        // Lógica chọn ghế, ví dụ: lưu trạng thái ghế vào Redis
    }

    public void releaseSeat(String showtimeId, String seatId) {
        // Lógica thả ghế, ví dụ: xóa trạng thái ghế khỏi Redis
    }
    public List<Seat> getSeatsByRoomId(Long roomId) {
        return seatRepository.getSeatsByRoomId(roomId);
    }
}