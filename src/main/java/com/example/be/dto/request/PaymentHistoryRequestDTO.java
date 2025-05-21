package com.example.be.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PaymentHistoryRequestDTO {
    private LocalDateTime dateTransaction;
    private Integer sumTicket;
    private BigDecimal sumPrice;
    private String methodPayment;
    private String status;
    private Long userId;
    private Long showtimeId;
}
