package com.example.be.service;

import com.example.be.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BookingCleanupService {
    @Autowired
    BookingRepository bookingRepository;

    @Scheduled(fixedRate = 5 * 60 * 1000) // 5 phút
    public void cleanupExpiredSelectedBookings() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(30);
        bookingRepository.deleteExpiredSelectedBookings(cutoffTime);
        System.out.println("Đã xóa các booking SELECTED quá 30 phút lúc " + LocalDateTime.now());
    }
}
