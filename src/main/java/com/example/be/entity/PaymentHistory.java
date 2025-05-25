package com.example.be.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "payment_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private LocalDateTime DateTransaction;

    private Integer SumTicket;

    private BigDecimal SumPrice;

    private String MethodPayment;

    private String Status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id", referencedColumnName = "showtimeId")
    @ToString.Exclude
    private ShowTime showTime;

    @OneToMany(mappedBy = "paymentHistory", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<PaymentDetail> paymentDetails;
}