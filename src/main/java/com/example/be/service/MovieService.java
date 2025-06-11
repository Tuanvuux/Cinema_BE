package com.example.be.service;

import com.example.be.entity.Movie;
import com.example.be.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }

    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    public long countMovies() {
        return movieRepository.countByIsDeleteFalse();
    }

    public LocalDate getReleaseDateByMovieId(Long movieId) {
        return movieRepository.findReleaseDateByMovieId(movieId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phim với ID: " + movieId));
    }

    public Boolean existsMovieIsDelete(Long movieId) {
        return movieRepository.getIsDeleteStatusByMovieId(movieId);
    }
}
