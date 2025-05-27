package com.example.be.service;

import com.example.be.dto.response.LockSeatAdminDTO;
import com.example.be.dto.response.SeatDTO;
import com.example.be.entity.*;
import com.example.be.repository.BookingRepository;
import com.example.be.repository.LockSeatByShowTimeRepository;
import com.example.be.repository.SeatRepository;
import com.example.be.repository.ShowTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatLockAdminService {
    @Autowired
    private LockSeatByShowTimeRepository lcstRepository;

    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private RoomService roomService;
    @Autowired
    private ShowTimeRepository showTimeRepository;
    @Autowired
    private SeatService seatService;
    @Autowired
    private BookingRepository bookingRepository;

    public List<LockSeatByShowTime> getLockSeatAdmin(){
        return lcstRepository.findAll();
    }

    public List<Seat> getSeatsByShowtimeId(Long showtimeId) {
        return seatRepository.findSeatsByShowtimeId(showtimeId);
    }
    public String deleteLockSeat(Long lockSeatId) {
        lcstRepository.deleteById(lockSeatId);
        return "Seat lock deleted";
    }

    public LockSeatAdminDTO convertToDTO(LockSeatByShowTime seat) {
        LockSeatAdminDTO dto = new LockSeatAdminDTO();
        dto.setLockSeatId(seat.getLockSeatId());
        dto.setStatus(seat.getStatus());
        if (seat.getRoom() != null) {
            dto.setRoomId(seat.getRoom().getId());
            dto.setRoomName(seat.getRoom().getName());
        }
        if(seat.getSeat() != null) {
            dto.setSeatId(seat.getSeat().getSeatId());
            dto.setSeatName(seat.getSeat().getSeatName());
        }

        return dto;
    }
    public LockSeatByShowTime convertToEntity(LockSeatAdminDTO dto) {
        LockSeatByShowTime seat = new LockSeatByShowTime();
        seat.setLockSeatId(dto.getLockSeatId());
        seat.setStatus(dto.getStatus());
        return seat;
    }

    public LockSeatAdminDTO addLockSeatAdminDTO(LockSeatAdminDTO seatDTO) {
        // Xử lý logic để chuyển từ DTO thành Entity và lưu
        LockSeatByShowTime seat = convertToEntity(seatDTO);

        if (seatDTO.getRoomId() != null) {
            Room room = roomService.getRoomById(seatDTO.getRoomId());
            seat.setRoom(room);
        }
        if (seatDTO.getShowtimeId() != null) {
            ShowTime st = showTimeRepository.findById(seatDTO.getShowtimeId())
                    .orElseThrow(() -> new RuntimeException("ShowTime not found"));
            seat.setShowtime(st);
        }
        if (seatDTO.getSeatId() != null) {
            Seat s = seatService.getSeatById(seatDTO.getSeatId());
            seat.setSeat(s);
        }

        LockSeatByShowTime savedSeat = lcstRepository.save(seat);
        return convertToDTO(savedSeat);
    }

    public boolean isSeatBooked(Long seatId) {
        return bookingRepository.existsBySeatId(seatId);
    }
}
