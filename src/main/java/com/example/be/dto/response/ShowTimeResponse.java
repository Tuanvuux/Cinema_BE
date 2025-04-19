package com.example.be.dto.response;
import com.example.be.entity.ShowTime;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class ShowTimeResponse {
    private Long movieId;
    private String movieName;
    private String posterUrl;
    List<ShowTime> showTimeList;

}
