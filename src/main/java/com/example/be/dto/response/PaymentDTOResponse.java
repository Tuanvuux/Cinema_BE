package com.example.be.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentDTOResponse {
    private  long paymentId;
    private BigDecimal sumPrice;
}
