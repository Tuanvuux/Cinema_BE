package com.example.be.controller;

import com.example.be.entity.Category;
import com.example.be.repository.CategoryRepository;
import com.example.be.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "http://localhost:5173")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        return ResponseEntity.ok(categoryService.addCategory(category));
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }

//    @PostMapping
//    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
//        Category savedCategory = categoryService.saveCategory(category);
//        return ResponseEntity.status(201).body(savedCategory);
//    }
}