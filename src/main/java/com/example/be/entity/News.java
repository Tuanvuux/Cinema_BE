package com.example.be.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "news")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Tiêu đề bài viết (hiển thị & SEO) */
    @Column(nullable = false, length = 150)
    private String title;

    /** Đường dẫn thân thiện – unique */
    @Column(nullable = false, unique = true, length = 180)
    private String slug;

    /** Ảnh bìa (URL tới S3/Cloudinary/… ) */
    private String coverImageUrl;

    /** Tóm tắt ngắn cho trang list/card */
    @Column(length = 400)
    private String summary;

    /** Nội dung chi tiết (HTML hoặc Markdown) */
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @CreationTimestamp
    private LocalDateTime createdAt;

    /** Quan hệ: bài News *có thể* gắn với 1 phim (tùy chọn) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;   // nullable ⇒ bài chung không gắn phim
}
