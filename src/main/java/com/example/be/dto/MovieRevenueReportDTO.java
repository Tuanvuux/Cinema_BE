package com.example.be.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieRevenueReportDTO {
    private Long movieId;
    private String movieName;
    private BigDecimal revenue;
}
