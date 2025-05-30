package com.example.be.dto.response;

import com.example.be.enums.PostCategory;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDTO {
    private Long id;
    private String title;
    private PostCategory category;
    private String introParagraph;
    private String conclusion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;
    private List<PostSectionResponseDTO> sections;
}
