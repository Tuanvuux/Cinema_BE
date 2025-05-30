package com.example.be.dto.request;
import com.example.be.dto.response.PostParagraphDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PostSectionDTO {
    private String heading;
    private List<PostParagraphDTO> paragraphs = new ArrayList<>();
    private List<String> images  = new ArrayList<>();
    private Integer sectionOrder;
}