package com.example.be.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "room")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int seatCount;
    private int numberOfcolumns;
    private int numberOfrows;
    private String screenType;
    private String status;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
