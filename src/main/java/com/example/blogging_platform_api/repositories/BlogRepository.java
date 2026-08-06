package com.example.blogging_platform_api.repositories;

import com.example.blogging_platform_api.models.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.logging.Logger;

@Repository
public class BlogRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BlogRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final Logger logger = Logger.getLogger(BlogRepository.class.getName());


    // adds blog post
    public Blog addBlog(Blog blog) {
        String sqlInsert = "INSERT INTO blogs (title, content, category, tags, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
        logger.info("Running: " + sqlInsert);
        blog.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        blog.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sqlInsert, new String[] {"id"});
            ps.setString(1, blog.getTitle());
            ps.setString(2, blog.getContent());
            ps.setString(3, blog.getCategory());
            String[] initBlogArray = blog.getTags() != null ? blog.getTags() : new String[0];
            Array blogArray = con.createArrayOf("text", initBlogArray);
            System.out.println();
            ps.setArray(4, blogArray);
            ps.setObject(5, blog.getCreatedAt());
            ps.setObject(6, blog.getUpdatedAt());
            return ps;
        }, keyHolder);

        if(keyHolder.getKey() != null) {
            blog.setId(keyHolder.getKey().intValue());
        }

        return blog;
    }


}
