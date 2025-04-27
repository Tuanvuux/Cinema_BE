package com.example.be.entity;
import com.example.be.enums.SeatStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;
    private String seatName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    @JsonIgnore
    private Room room; // Phòng chiếu mà ghế này thuộc về

    private String rowLabel; // Thay "row" bằng "rowLabel"
    private int columnNumber; // Cột ghế (1, 2, 3...)
    @Enumerated(EnumType.STRING)
    private SeatStatus status; // Trạng thái ghế (AVAILABLE, SELECTED, BOOKED)

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seat_info_id", referencedColumnName = "id")
    private SeatInfo seatInfo;

}

