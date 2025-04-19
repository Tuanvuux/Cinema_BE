package com.example.be.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SeatSelectionMessage {
    private String seatId;
    private String showtimeId;
    private String userId;
}