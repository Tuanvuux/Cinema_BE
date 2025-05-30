package com.example.be.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;
    private String caption;
    private Integer imageOrder;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private PostSection section;

}
