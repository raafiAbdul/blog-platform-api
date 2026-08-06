package com.example.blogging_platform_api.controllers;

import com.example.blogging_platform_api.models.Blog;
import com.example.blogging_platform_api.repositories.BlogRepository;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/posts")
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
        } catch (Exception _) {
            return ResponseEntity.badRequest().body("Please make sure that the title is unique and that there are no missing parameters");
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putBlog(@RequestBody Blog blog, @PathVariable int id) {
        try {
            return ResponseEntity.ok().body(blogRepository.updateBlog(blog, id));
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.toString());
        }
    }
}
