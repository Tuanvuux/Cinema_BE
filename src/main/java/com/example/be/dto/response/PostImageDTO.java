package com.example.be.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostImageDTO {
    private Long id;
    private String url;
    private Integer imageOrder;
}
