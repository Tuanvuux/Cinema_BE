package com.example.be.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class ExtendLockRequest {
    private Long showtimeId;
    private Long userId;
    private List<Long> seatIds;
    private int extendTime;
}
