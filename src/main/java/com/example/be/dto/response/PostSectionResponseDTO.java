package com.example.be.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostSectionResponseDTO {
    private Long id;
    private String heading;
    private Integer sectionOrder;
    private List<PostParagraphDTO> paragraphs;
    private List<PostImageDTO> images;
}
