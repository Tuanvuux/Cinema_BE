package com.example.be.repository;

import com.example.be.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Modifying
    @Query("DELETE FROM PostSection ps WHERE ps.post.id = :postId")
    void deleteAllSectionsByPostId(@Param("postId") Long postId);
}
