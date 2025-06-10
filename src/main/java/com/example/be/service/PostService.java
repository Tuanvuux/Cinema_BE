package com.example.be.service;

import com.example.be.dto.request.CreatePostRequest;
import com.example.be.dto.response.PostImageDTO;
import com.example.be.dto.response.PostParagraphDTO;
import com.example.be.dto.response.PostResponseDTO;
import com.example.be.dto.response.PostSectionResponseDTO;
import com.example.be.entity.*;
import com.example.be.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Post createPost(CreatePostRequest request) {
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setPostImage(request.getPostImage());
        post.setIntroParagraph(request.getIntroParagraph());
        post.setConclusion(request.getConclusion());
        post.setCreatedBy(request.getCreatedBy());
        post.setUpdatedBy(request.getCreatedBy()); // lúc tạo lần đầu
        post.setSections(new ArrayList<>());

        if (request.getSections() != null) {
            for (var sectionDTO : request.getSections()) {
                PostSection section = new PostSection();
                section.setHeading(sectionDTO.getHeading());
                section.setPost(post);

                List<PostParagraph> paragraphs = new ArrayList<>();
                for (PostParagraphDTO paraDTO : sectionDTO.getParagraphs()) {
                    PostParagraph paragraph = new PostParagraph();
                    paragraph.setContent(paraDTO.getContent());
                    paragraph.setParagraphOrder(paraDTO.getParagraphOrder()); // nếu có trường này trong entity
                    paragraph.setSection(section);
                    paragraphs.add(paragraph);
                }

                List<PostImage> images = new ArrayList<>();
                int order = 1;
                for (String url : sectionDTO.getImages()) {
                    PostImage image = new PostImage();
                    image.setImageUrl(url);
                    image.setSection(section);
                    image.setImageOrder(order++);  // Set thứ tự ảnh
                    images.add(image);
                }
                section.setParagraphs(paragraphs);
                section.setImages(images);
                post.getSections().add(section);
            }
        }

        return postRepository.save(post);
    }
    public PostResponseDTO mapToPostResponseDTO(Post post) {
        return new PostResponseDTO(
                post.getId(),
                post.getTitle(),
                post.getPostImage(),
                post.getIntroParagraph(),
                post.getConclusion(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getCreatedBy(),
                post.getUpdatedBy(),
                post.getSections().stream().map(section -> new PostSectionResponseDTO(
                        section.getId(),
                        section.getHeading(),
                        section.getSectionOrder(),
                        section.getParagraphs().stream().map(p -> new PostParagraphDTO(
                                p.getId(), p.getContent(), p.getParagraphOrder()
                        )).toList(),
                        section.getImages().stream().map(i -> new PostImageDTO(
                                i.getId(), i.getImageUrl(), i.getImageOrder()
                        )).toList()
                )).toList()
        );
    }
    @Transactional(readOnly = true)
    public List<PostResponseDTO> getAllPosts() {
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();
        return posts.stream()
                .map(this::mapToPostResponseDTO)
                .toList();
    }
    public PostResponseDTO getPostById(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isEmpty()) {
            throw new RuntimeException("Không tìm thấy bài viết với id: " + id);
        }

        Post post = optionalPost.get();
        // Chuyển Post entity thành DTO
        return mapToPostResponseDTO(post);
    }
    @Transactional
    public Post updatePost(Long id, CreatePostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết với id: " + id));

        post.setTitle(request.getTitle());
        post.setPostImage(request.getPostImage());
        post.setIntroParagraph(request.getIntroParagraph());
        post.setConclusion(request.getConclusion());
        post.setUpdatedBy(request.getUpdatedBy());
        post.setUpdatedAt(LocalDateTime.now());

        // Xóa các section cũ một cách an toàn (orphanRemoval = true)
        List<PostSection> existingSections = post.getSections();
        existingSections.clear();

        if (request.getSections() != null) {
            for (var sectionDTO : request.getSections()) {
                PostSection section = new PostSection();
                section.setHeading(sectionDTO.getHeading());
                section.setSectionOrder(sectionDTO.getSectionOrder());
                section.setPost(post); // liên kết ngược

                // Paragraphs
                List<PostParagraph> paragraphs = new ArrayList<>();
                for (PostParagraphDTO paraDTO : sectionDTO.getParagraphs()) {
                    PostParagraph paragraph = new PostParagraph();
                    paragraph.setContent(paraDTO.getContent());
                    paragraph.setParagraphOrder(paraDTO.getParagraphOrder());
                    paragraph.setSection(section); // liên kết ngược
                    paragraphs.add(paragraph);
                }

                // Images
                List<PostImage> images = new ArrayList<>();
                int order = 1;
                for (String url : sectionDTO.getImages()) {
                    PostImage image = new PostImage();
                    image.setImageUrl(url);
                    image.setImageOrder(order++);
                    image.setSection(section); // liên kết ngược
                    images.add(image);
                }

                section.setParagraphs(paragraphs);
                section.setImages(images);

                existingSections.add(section); // thêm vào danh sách hiện tại
            }
        }

        return postRepository.save(post);
    }


    public String deletedPost(Long id){
        postRepository.deleteById(id);
        return "Post deleted successfully!";
    }
}
