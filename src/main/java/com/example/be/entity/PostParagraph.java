package com.example.be.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_paragraph")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostParagraph {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String content;
    private Integer paragraphOrder;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private PostSection section;

    // Getters, Setters
}
