package com.example.be.repository;

import com.example.be.entity.SeatInfo;
import com.example.be.enums.SeatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeatInfoRepository extends JpaRepository<SeatInfo, Long> {
    Optional<SeatInfo> findByName(SeatType name);
}
