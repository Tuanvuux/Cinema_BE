package com.example.be.dto.response;

import com.example.be.entity.Movie;
import com.example.be.entity.Room;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ShowTimeAdminDTO {

    private Long showtimeId;
    private LocalDate showDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long movieId;
    private String movieName;
    private Long roomId;
    private String roomName;
}
