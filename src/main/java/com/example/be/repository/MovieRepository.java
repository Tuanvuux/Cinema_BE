package com.example.be.repository;

import com.example.be.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface  MovieRepository  extends JpaRepository<Movie, Long> {
    long countByIsDeleteFalse();
    @Query("SELECT m.releaseDate FROM Movie m WHERE m.movieId = :movieId AND (m.isDelete = false OR m.isDelete IS NULL)")
    Optional<LocalDate> findReleaseDateByMovieId(@Param("movieId") Long movieId);

    @Query("SELECT m.isDelete FROM Movie m WHERE m.movieId = :movieId")
    Boolean getIsDeleteStatusByMovieId(@Param("movieId") Long movieId);
}
