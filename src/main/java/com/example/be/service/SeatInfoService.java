package com.example.be.service;

import com.example.be.entity.SeatInfo;
import com.example.be.repository.SeatInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatInfoService {

    @Autowired
    private SeatInfoRepository seatInfoRepository;

    public List<SeatInfo> getAllSeatInfo() {
        return seatInfoRepository.findAll();
    }

    public SeatInfo getSeatInfoById(Long id) {
        return seatInfoRepository.findById(id).orElseThrow(() -> new RuntimeException("SeatInfo not found"));
    }

    public SeatInfo addSeatInfo(SeatInfo seatInfo) {
        return seatInfoRepository.save(seatInfo);
    }

    public SeatInfo updateSeatInfo(Long id,SeatInfo seatInfo) {
        SeatInfo oldSeatInfo = getSeatInfoById(id);
        oldSeatInfo.setPrice(seatInfo.getPrice());
        return seatInfoRepository.save(oldSeatInfo);
    }

    public String deleteSeatInfo(Long id) {
        seatInfoRepository.deleteById(id);
        return "Room deleted successfully!";
    }
}
