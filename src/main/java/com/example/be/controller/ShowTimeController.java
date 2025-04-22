package com.example.be.controller;
import com.example.be.dto.response.ShowTimeAdminDTO;
import com.example.be.dto.response.ShowTimeResponse;
import com.example.be.entity.Movie;
import com.example.be.entity.Room;
import com.example.be.entity.ShowTime;
import com.example.be.service.MovieService;
import com.example.be.service.RoomService;
import com.example.be.service.ShowTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/showtime")
@CrossOrigin(origins = "http://localhost:5173")
public class ShowTimeController {
    @Autowired
    private ShowTimeService showTimeService;

    @Autowired
    private MovieService movieservice;

    @Autowired
    private RoomService roomservice;

    @PostMapping
    public ResponseEntity<?> addShowtime(@RequestBody ShowTime showtimeData) {
        try {
            Optional<Movie> movieOpt = movieservice.getMovieById(showtimeData.getMovie().getMovieId());
            if (movieOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid Movie ID!");
            }
            showtimeData.setMovie(movieOpt.get());

            Optional<Room> roomOpt = Optional.ofNullable(roomservice.getRoomById(showtimeData.getRoom().getId()));
            if (roomOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid Room ID!");
            }
            showtimeData.setRoom(roomOpt.get());

            ShowTime savedShowtime = showTimeService.saveShowtime(showtimeData);
            return ResponseEntity.status(201).body(savedShowtime);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ShowTime updateShowtime(@PathVariable Long id, @RequestBody ShowTime showtime) {
        return showTimeService.updateShowtime(id, showtime);
    }

    @DeleteMapping("/{id}")
    public String deleteShowtime(@PathVariable Long id) {
        return showTimeService.deletedShowtime(id);
    }
    @GetMapping("")
    public ResponseEntity<List<ShowTimeResponse>> getAllShowTime() {
        return ResponseEntity.ok(showTimeService.findAllShowTime());}
    @GetMapping("admin")
    public List<ShowTimeAdminDTO> getShowtime() {
        return showTimeService.getAllShowtimes();
    }
}
