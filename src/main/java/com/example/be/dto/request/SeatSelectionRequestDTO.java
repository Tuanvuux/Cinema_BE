package com.example.be.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeatSelectionRequestDTO {
    private Long showtimeId;
    private Long seatId;
    private Long userId;
}
