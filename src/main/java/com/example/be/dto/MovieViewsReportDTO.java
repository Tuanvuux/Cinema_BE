package com.example.be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieViewsReportDTO {
    private Long movieId;
    private String movieName;
    private Integer ticketCount;
}
