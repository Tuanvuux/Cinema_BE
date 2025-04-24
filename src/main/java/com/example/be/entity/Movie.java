package com.example.be.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "movie")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;

    private String name;
    private String director;
    private String actor;
    private String description;
    private String country;
    private int duration;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    private String caption;
    private String posterUrl;
    private String trailerUrl;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private String bannerUrl;
    private int ageLimit;
    @Column(name = "is_delete")
    @Nullable
    private Boolean isDelete = false;

    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;

}
