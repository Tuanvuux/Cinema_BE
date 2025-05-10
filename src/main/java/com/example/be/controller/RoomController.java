package com.example.be.controller;

import com.example.be.entity.Room;
import com.example.be.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@CrossOrigin(origins = "http://localhost:5173")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PostMapping("/admin")
    public Room addRoom(@RequestBody Room room) {
        return roomService.addRoom(room);
    }

    @GetMapping("/admin")
    public List<Room> getRooms() {
        return roomService.getRooms();
    }

    @GetMapping("/admin/{id}")
    public Room getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id);
    }

    @PutMapping("/admin/{id}")
    public Room updateRoom(@PathVariable Long id, @RequestBody Room room) {
        return roomService.updateRoom(id, room);
    }

    @DeleteMapping("/admin/{id}")
    public String deleteRoom(@PathVariable Long id) {
        return roomService.deleteRoom(id);
    }

    @GetMapping("/{id}")
    public Room getRoomByRoomId(@PathVariable Long id) {
        return roomService.getRoomById(id);
    }
    @GetMapping("/admin/countroom")
    public long sumRoom() {
        return roomService.countRoom();
    }
}

