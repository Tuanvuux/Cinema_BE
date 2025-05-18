package com.example.be.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class BookingRequestDTO {

    @NotNull
    private Long userId;

    @NotNull
    private Long showtimeId;

    @NotEmpty
    private List<Long> seatIds;
}
