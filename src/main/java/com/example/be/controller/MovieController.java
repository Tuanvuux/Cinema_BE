package com.example.be.controller;

import com.example.be.entity.Movie;
import com.example.be.entity.Category;
import com.example.be.exception.CustomerException;
import com.example.be.repository.MovieRepository;
import com.example.be.service.MovieService;
import com.example.be.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/movies")
@CrossOrigin(origins = "http://localhost:5173")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @Autowired
    private CategoryService categoryService; // Dịch vụ lấy thể loại
    private MovieRepository movieRepository;

    @GetMapping
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Optional<Movie> movie = movieService.getMovieById(id);
        return movie.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createMovie(@RequestBody Movie movie) {
        if (movie.getCategory() == null || movie.getCategory().getCategoryId() == null) {
            return ResponseEntity.badRequest().body("Category ID is required!");
        }

        Optional<Category> categoryOpt = categoryService.getCategoryById(movie.getCategory().getCategoryId());
        if (categoryOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid category ID!");
        }

        movie.setCategory(categoryOpt.get());
        movie.setCreatedAt(LocalDateTime.now());
        if (movie.getIsDelete() == null) {
            movie.setIsDelete(false);
        }
        Movie savedMovie = movieService.saveMovie(movie);
        return ResponseEntity.status(201).body(savedMovie);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
        Optional<Movie> movieOpt = movieService.getMovieById(id);
        if (movieOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid movie ID!");
        }
        movie.setMovieId(movieOpt.get().getMovieId());
        Optional<Category> categoryOpt = categoryService.getCategoryById(movie.getCategory().getCategoryId());
        if (categoryOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid category ID!");
        }
        movie.setCategory(categoryOpt.get());
        movie.setCreatedAt(LocalDateTime.now());
        if (movie.getIsDelete() == null) {
            movie.setIsDelete(false);
        }
        Movie updateMovie = movieService.saveMovie(movie);
        return ResponseEntity.status(201).body(updateMovie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle-delete")
    public ResponseEntity<Movie> toggleDeleteStatus(@PathVariable("id") Long movieId, @RequestBody Map<String, Boolean> statusMap) {
        Boolean isDelete = statusMap.get("isDelete");
        if (isDelete == null) {
            return ResponseEntity.badRequest().build();
        }

        Movie movie = movieService.getMovieById(movieId)
                .orElseThrow(() -> new CustomerException("Movie not found with id: " + movieId));

        movie.setIsDelete(isDelete);
        Movie updatedMovie = movieService.saveMovie(movie);

        return ResponseEntity.ok(updatedMovie);
    }
}
