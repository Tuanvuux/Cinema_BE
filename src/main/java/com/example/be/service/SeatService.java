package com.example.be.service;

import com.example.be.dto.response.SeatDTO;
import com.example.be.dto.response.SeatWithLockResponse;
import com.example.be.entity.Room;
import com.example.be.entity.Seat;
import com.example.be.entity.ShowTime;
import com.example.be.repository.SeatRepository;
import com.example.be.repository.ShowTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private RoomService roomService;

    @Autowired
    private SeatLockService seatLockService;

    @Autowired
    private ShowTimeRepository showTimeRepository;

    public void selectSeat(String showtimeId, String seatId) {
        // Lógica chọn ghế, ví dụ: lưu trạng thái ghế vào Redis
    }

    public void releaseSeat(String showtimeId, String seatId) {
        // Lógica thả ghế, ví dụ: xóa trạng thái ghế khỏi Redis
    }

    public List<Seat> getSeatsByRoomId(Long roomId) {
        return seatRepository.getSeatsByRoomId(roomId);
    }

    public List<Seat> getSeats() {
        return seatRepository.findAll();
    }

    public Seat getSeatById(Long seatId) {
        return seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found"));
    }

    public Seat addSeat(Seat seat) {
        return seatRepository.save(seat);
    }

    public String deleteSeat(Long seatId) {
        seatRepository.deleteById(seatId);
        return "Seat deleted successfully!";
    }

    public List<SeatDTO> getSeatsWithRoomInfo() {
        List<Seat> seats = seatRepository.findAll();
        return seats.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public SeatDTO convertToDTO(Seat seat) {
        SeatDTO dto = new SeatDTO();
        dto.setSeatId(seat.getSeatId());
        dto.setSeatName(seat.getSeatName());

        // Kiểm tra phòng và đặt thông tin phòng nếu có
        if (seat.getRoom() != null) {
            dto.setRoomId(seat.getRoom().getId());
            dto.setRoomName(seat.getRoom().getName());
        }

        dto.setRowLabel(seat.getRowLabel());
        dto.setColumnNumber(seat.getColumnNumber());
        dto.setStatus(seat.getStatus());
        dto.setSeatType(seat.getSeatType());

        return dto;
    }

    // Thêm phương thức này nếu bạn cần chuyển đổi DTO thành Entity
    public Seat convertToEntity(SeatDTO dto) {
        Seat seat = new Seat();
        seat.setSeatId(dto.getSeatId());
        seat.setSeatName(dto.getSeatName());
        seat.setRowLabel(dto.getRowLabel());
        seat.setColumnNumber(dto.getColumnNumber());
        seat.setStatus(dto.getStatus());
        seat.setSeatType(dto.getSeatType());
        // Lưu ý: Room sẽ được thiết lập riêng

        return seat;
    }

    public SeatDTO addSeatDTO(SeatDTO seatDTO) {
        // Xử lý logic để chuyển từ DTO thành Entity và lưu
        Seat seat = convertToEntity(seatDTO);

        // Thiết lập Room cho Seat dựa trên roomId trong DTO
        if (seatDTO.getRoomId() != null) {
            Room room = roomService.getRoomById(seatDTO.getRoomId());
            seat.setRoom(room);
        }

        Seat savedSeat = seatRepository.save(seat);
        return convertToDTO(savedSeat);
    }

    public SeatDTO updateSeat(Long id, SeatDTO seatDTO) {
        // Tìm ghế hiện tại
        Seat existingSeat = getSeatById(id);

        // Cập nhật thông tin từ DTO
        existingSeat.setSeatName(seatDTO.getSeatName());
        existingSeat.setRowLabel(seatDTO.getRowLabel());
        existingSeat.setColumnNumber(seatDTO.getColumnNumber());
        existingSeat.setStatus(seatDTO.getStatus());
        existingSeat.setSeatType(seatDTO.getSeatType());

        // Cập nhật room nếu được cung cấp
        if (seatDTO.getRoomId() != null) {
            Room room = roomService.getRoomById(seatDTO.getRoomId());
            existingSeat.setRoom(room);
        }

        Seat updatedSeat = seatRepository.save(existingSeat);
        return convertToDTO(updatedSeat);
    }

    public List<SeatWithLockResponse> getSeatsWithLock(Long showtimeId) {
        // Lấy suất chiếu
        ShowTime showtime = showTimeRepository.findById(showtimeId)
                .orElseThrow(() -> new RuntimeException("Showtime not found"));

        Long roomId = showtime.getRoom().getId(); // lấy ID của phòng chiếu từ suất chiếu

        // Lấy danh sách ghế của phòng chiếu đó
        List<Seat> seats = seatRepository.getSeatsByRoomId(roomId);

        return seats.stream().map(seat -> {
            boolean isLocked = seatLockService.isSeatLocked(showtimeId, seat.getSeatId());
            return new SeatWithLockResponse(
                    seat.getSeatId(),
                    seat.getSeatName(),
                    seat.getRowLabel(),
                    seat.getColumnNumber(),
                    seat.getStatus(),
                    seat.getSeatType(),
                    isLocked
            );
        }).collect(Collectors.toList());
    }


}