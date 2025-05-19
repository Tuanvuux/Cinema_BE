package com.example.be.repository;

import com.example.be.entity.LockSeatByShowTime;
import com.example.be.entity.Seat;
import com.example.be.entity.ShowTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LockSeatByShowTimeRepository extends JpaRepository<LockSeatByShowTime,Long> {

}
