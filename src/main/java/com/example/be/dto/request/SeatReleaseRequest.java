package com.example.be.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SeatReleaseRequest {
    private String showtimeId;
    private String seatId;

}