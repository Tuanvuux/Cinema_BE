package com.example.be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDetailReportDTO {
    private Long movieId;
    private String movieName;
    private BigDecimal revenue;
    private Integer ticketCount;
    private Integer showtimeCount;
    private Integer occupancyRate;
}
