package com.example.be.dto.response;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ShowTimeDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long showtimeId;
    private LocalDate showDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long movieId;
    private Long roomId;
    private String roomName;

}
