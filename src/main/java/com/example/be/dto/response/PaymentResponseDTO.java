package com.example.be.dto.response;

import com.example.be.dto.response.PaymentDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {
    private Long paymentId;
    private LocalDateTime dateTransaction;
    private Integer sumTicket;
    private BigDecimal sumPrice;
    private String methodPayment;

    // User information
    private Long userId;
    private String username;

    private Long roomId;
    private String roomName;

    // Movie and schedule information
    private String movieName;
    private Long scheduleId;

    // Seat information (concatenated seat names)
    private String seatNames;

    private List<PaymentDetailDTO> paymentDetails;
}