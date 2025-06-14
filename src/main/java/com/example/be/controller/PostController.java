package com.example.be.controller;

import com.example.be.dto.request.CreatePostRequest;
import com.example.be.dto.response.PostResponseDTO;
import com.example.be.entity.Post;
import com.example.be.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/admin")
    public ResponseEntity<PostResponseDTO> createPost(@RequestBody CreatePostRequest dto) {
        Post post = postService.createPost(dto);
        PostResponseDTO response = postService.mapToPostResponseDTO(post);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getPost() {
        List<PostResponseDTO> response = postService.getAllPosts();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/admin/{postId}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long postId) {
        PostResponseDTO response = postService.getPostById(postId);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/admin/{postId}")
    public ResponseEntity<PostResponseDTO> updatePost(@PathVariable Long postId, @RequestBody CreatePostRequest dto) {
        PostResponseDTO response;
        try {
            Post post = postService.updatePost(postId, dto);
            response = postService.mapToPostResponseDTO(post);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/admin/{postId}")
    public String deletePost(@PathVariable Long postId) {
        return postService.deletedPost(postId);
    }
}
