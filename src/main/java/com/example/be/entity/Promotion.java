package com.example.be.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "promotion")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Tiêu đề ưu đãi */
    @Column(nullable = false, length = 150)
    private String title;

    /** Đường dẫn thân thiện */
    @Column(nullable = false, unique = true, length = 180)
    private String slug;

    /** Banner chính (URL) */
    private String bannerUrl;

    /** Mô tả ngắn hiển thị trên trang list */
    @Column(length = 400)
    private String description;

    /** Điều kiện – điều khoản (hiển thị trang chi tiết) */
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String rules;

    private LocalDate startDate;
    private LocalDate endDate;

    @CreationTimestamp
    private LocalDate createdAt;

    /** Quan hệ N-N: 1 ưu đãi áp dụng cho nhiều phim (tùy chọn) */
    @ManyToMany
    @JoinTable(name = "promotion_movie",
            joinColumns = @JoinColumn(name = "promotion_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"))
    private Set<Movie> movies = new HashSet<>();

    /** Đánh dấu ẩn/hiện (soft delete) */
    private Boolean isActive = true;
}
