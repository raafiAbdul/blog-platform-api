package com.example.blogging_platform_api.controllers;

import com.example.blogging_platform_api.models.Blog;
import com.example.blogging_platform_api.repositories.BlogRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping("/blogs")
public class BlogController {
    private final BlogRepository blogRepository;

    public BlogController(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @PostMapping
    public ResponseEntity<?> postBlog(@RequestBody Blog blog) {
        try {
            Blog responseBlog = blogRepository.addBlog(blog);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseBlog);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }

    }
}
