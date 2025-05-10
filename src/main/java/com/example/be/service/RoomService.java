package com.example.be.service;

import com.example.be.entity.Room;
import com.example.be.repository.RoomRepository;
import com.example.be.repository.ShowTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ShowTimeRepository showTimeRepository;

    public Room addRoom(Room room) {
        return roomRepository.save(room);
    }

    public List<Room> getRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }

    public Room updateRoom(Long id, Room roomDetails) {
        Room room = getRoomById(id);
        room.setName(roomDetails.getName());
        room.setSeatCount(roomDetails.getSeatCount());
        room.setNumberOfColumns(roomDetails.getNumberOfColumns());
        room.setNumberOfRows(roomDetails.getNumberOfRows());
        room.setStatus(roomDetails.getStatus());
        return roomRepository.save(room);
    }

    public String deleteRoom(Long id) {
        roomRepository.deleteById(id);
        return "Room deleted successfully!";
    }
    public long countRoom(){
        return roomRepository.count();
    }

}
