package com.example.be.dto.response;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostParagraphDTO {
    private Long id;
    private String content;
    private Integer paragraphOrder;
}
