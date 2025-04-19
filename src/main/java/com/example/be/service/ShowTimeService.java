package com.example.be.service;

import com.example.be.dto.response.ShowTimeResponse;
import com.example.be.entity.Movie;
import com.example.be.entity.Room;
import com.example.be.entity.ShowTime;
import com.example.be.repository.MovieRepository;
import com.example.be.repository.RoomRepository;
import com.example.be.repository.ShowTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
public class ShowTimeService {
    @Autowired
    private ShowTimeRepository showTimeRepository;

    @Autowired
    private MovieRepository movieRepository;


    @Autowired
    private RoomRepository roomRepository;


    public ShowTime addShowtime(ShowTime showtime){
        return showTimeRepository.save(showtime);
    }


    public ShowTime getShowtimeId(Long id){
        return showTimeRepository.findById(id).orElseThrow(() -> new RuntimeException("Showtime not found"));
    }

    public ShowTime updateShowtime(Long id, ShowTime showtimeDetails) {
        ShowTime existingShowtime = getShowtimeId(id);

        // Lấy movie từ database dựa theo ID
        Movie movie = movieRepository.findById(showtimeDetails.getMovie().getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        existingShowtime.setMovie(movie);

        // Lấy room từ database dựa theo ID
        Room room = roomRepository.findById(showtimeDetails.getRoom().getId())
                .orElseThrow(() -> new RuntimeException("Room not found"));
        existingShowtime.setRoom(room);
        existingShowtime.setShowDate(showtimeDetails.getShowDate());
        existingShowtime.setStartTime(showtimeDetails.getStartTime());
        existingShowtime.setEndTime(showtimeDetails.getEndTime());

        return showTimeRepository.save(existingShowtime);
    }

    public String deletedShowtime(Long id){
        showTimeRepository.deleteById(id);
        return "Showtime deleted successfully!";
    }

    public ShowTime saveShowtime(ShowTime showtime){
        return showTimeRepository.save(showtime);
    }
    public List<ShowTimeResponse> findAllShowTime() {
        LocalDate today = LocalDate.now();

        // Lấy tất cả ShowTime với thông tin Movie và Room đã được JOIN FETCH
        List<ShowTime> showTimes = showTimeRepository.findAllWithMovieAndRoom(today);

        // Tạo map để gom các ShowTime theo movieId
        Map<Long, ShowTimeResponse> movieShowTimeMap = new HashMap<>();

        // Duyệt qua danh sách ShowTime để gom vào movieShowTimeMap
        for (ShowTime showTime : showTimes) {
            Long movieId = showTime.getMovie().getMovieId();

            // Nếu chưa có movie trong map thì thêm mới
            if (!movieShowTimeMap.containsKey(movieId)) {
                Movie movie = showTime.getMovie(); // Thông tin Movie đã được JOIN FETCH sẵn
                Room room = showTime.getRoom(); // Thông tin Room đã được JOIN FETCH sẵn

                ShowTimeResponse dto = new ShowTimeResponse();
                dto.setMovieId(movieId);
                dto.setMovieName(movie.getName());
                dto.setPosterUrl(movie.getPosterUrl());
                dto.setShowTimeList(new ArrayList<>());
                movieShowTimeMap.put(movieId, dto);
            }

            // Thêm ShowTime vào list của movieId
            movieShowTimeMap.get(movieId).getShowTimeList().add(showTime);
        }

        return new ArrayList<>(movieShowTimeMap.values());
    }
}
