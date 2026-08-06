package com.example.blogging_platform_api.controllers;

import com.example.blogging_platform_api.models.Blog;
import com.example.blogging_platform_api.repositories.BlogRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            Blog responseBlog = blogRepository.addPost(blog);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseBlog);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Please make sure that the title is unique " +
                    "and that there are no missing parameters (title, content, category, and tags).");
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putBlog(@RequestBody Blog blog, @PathVariable int id) {
        try {
            return ResponseEntity.ok().body(blogRepository.updatePost(blog, id));
        } catch(DuplicateKeyException e) {
            return ResponseEntity.badRequest().body("Title already exists.");
        } catch(Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBlog(@PathVariable int id) {
        try {
            blogRepository.deletePost(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneBlog(@PathVariable int id) {
        try {
            return ResponseEntity.ok().body(blogRepository.getOnePost(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<?> getBlog() {
        return ResponseEntity.ok().body(blogRepository.getAllPosts());
    }
}
