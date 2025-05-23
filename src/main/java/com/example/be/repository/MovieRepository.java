package com.example.be.repository;

import com.example.be.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface  MovieRepository  extends JpaRepository<Movie, Long> {
    long countByIsDeleteFalse();
}
