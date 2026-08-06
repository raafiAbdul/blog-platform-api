package com.example.blogging_platform_api.repositories;

import com.example.blogging_platform_api.models.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Repository
public class BlogRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BlogRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addBlog(Blog blog) {
        String now = LocalDateTime.now().format(DateTimeFormatter.ISO_INSTANT);
        String sqlInsert = "INSERT INTO * blogs (title, content, category, tags, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
        blog.setCreatedAt(now);
        blog.setUpdatedAt(now);
        jdbcTemplate.update(sqlInsert, blog.getTitle(), blog.getContent(),
                blog.getCategory(), blog.getTags(), blog.getCreatedAt(), blog.getUpdatedAt());
    }
}
