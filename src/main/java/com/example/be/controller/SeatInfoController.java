package com.example.be.controller;

import com.example.be.entity.Room;
import com.example.be.entity.SeatInfo;
import com.example.be.service.SeatInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seatinfo")
@CrossOrigin(origins = "http://localhost:5173")
public class SeatInfoController {
    @Autowired
    private SeatInfoService seatInfoService;

    @PostMapping("/admin")
    public SeatInfo addSeatInfo(@RequestBody SeatInfo seatInfo) {
        return seatInfoService.addSeatInfo(seatInfo);
    }

    @GetMapping("/admin")
    public List<SeatInfo> getAllSeatInfo() {
        return seatInfoService.getAllSeatInfo();
    }

    @GetMapping("/admin/{id}")
    public SeatInfo getSeatInfoById(@PathVariable Long id) {
        return seatInfoService.getSeatInfoById(id);
    }

    @PutMapping("/admin/{id}")
    public SeatInfo updateSeatInfo(@PathVariable Long id, @RequestBody SeatInfo seatInfo) {
        return seatInfoService.updateSeatInfo(id, seatInfo);
    }

    @DeleteMapping("/admin/{id}")
    public String deleteSeatInfo(@PathVariable Long id) {
        return seatInfoService.deleteSeatInfo(id);
    }
}
