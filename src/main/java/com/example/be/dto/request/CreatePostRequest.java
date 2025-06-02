package com.example.be.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CreatePostRequest {
    private String title;
    private String postImage;
    private String introParagraph;
    private String conclusion;
    private List<PostSectionDTO> sections;
    private Long createdBy; // ID người đăng
    private Long updatedBy;
}
