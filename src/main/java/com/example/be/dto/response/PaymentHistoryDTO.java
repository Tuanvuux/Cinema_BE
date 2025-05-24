package com.example.be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentHistoryDTO {
    private Long paymentId;
    private LocalDateTime dateTransaction;
    private Integer sumTicket;
    private BigDecimal sumPrice;
    private String methodPayment;
    private String status;
    private String movieTitle;
    private String roomName;
    private LocalDate showDate;
    private LocalTime startTime;
    private long userId;

    private List<PaymentDetailDTO> paymentDetails;
}
