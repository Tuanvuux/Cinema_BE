package com.example.be.dto.request;

import com.example.be.enums.PostCategory;
import lombok.Data;

import java.util.List;

@Data
public class CreatePostRequest {
    private String title;
    private PostCategory category;
    private String introParagraph;
    private String conclusion;
    private List<PostSectionDTO> sections;
    private Long createdBy; // ID người đăng
    private Long updatedBy;
}
