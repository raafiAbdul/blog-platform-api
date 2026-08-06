package com.example.blogging_platform_api.controllers;

import com.example.blogging_platform_api.models.Blog;
import com.example.blogging_platform_api.repositories.BlogRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blogs")
public class BlogController {
    private final BlogRepository blogRepository;

    public BlogController(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @PostMapping
    public void postBlog(@RequestBody Blog blog) {
        blogRepository.addBlog(blog);
    }
}
