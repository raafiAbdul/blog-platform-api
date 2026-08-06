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
import java.util.Map;
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
        String sqlInsert = "INSERT INTO posts (title, content, category, tags, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
        logger.info("Running: " + sqlInsert);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        // customize the PreparedStatementCreator functional interface
        blog.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        blog.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sqlInsert, new String[] {"id"}); // returns columns w/ generated value
            ps.setString(1, blog.getTitle());
            ps.setString(2, blog.getContent());
            ps.setString(3, blog.getCategory());
            String[] initBlogArray = blog.getTags() != null ? blog.getTags() : new String[0];
            Array blogArray = con.createArrayOf("text", initBlogArray);
            ps.setArray(4, blogArray);
            ps.setObject(5, blog.getCreatedAt());
            ps.setObject(6, blog.getUpdatedAt());
            return ps;
        }, keyHolder);

        // sets generated value to newly created blog id
        if(keyHolder.getKey() != null) {
            blog.setId(keyHolder.getKey().intValue());
        }

        return blog;
    }

    // updates blog post
    public Blog updateBlog(Blog blog, int id) {

        // configure the sql statement
        StringBuilder sqlUpdateAll = new StringBuilder("UPDATE posts SET ");
        String[] columns = new String[4];
        int nullCounter = 0;

        if(blog.getTitle() != null) {
            columns[0] = "title";
            nullCounter++;
        }
        if(blog.getContent() != null) {
            columns[1] = "content";
            nullCounter++;
        }
        if(blog.getCategory() != null) {
            columns[2] = "category";
            nullCounter++;
        }
        if(blog.getTags().length == 0 || blog.getTags() != null) {
            columns[3] = "tags";
            nullCounter++;
        }

        for(int i = 0; i < columns.length; i++) {
            if(columns[i] != null) {
                if(nullCounter == 1) {
                    sqlUpdateAll.append(columns[i] + "=?");
                } else {
                    sqlUpdateAll.append(columns[i] + "=?,");
                    nullCounter--;
                }
            }
        }
        sqlUpdateAll.append(" WHERE id = " + id);
        logger.info("Running: " + sqlUpdateAll);

        // update row
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sqlUpdateAll.toString(), new String[] {"id", "content", "category", "tags", "created_at"});
            int bindVariableCounter = 1;
            // UPDATE posts SET title = ?, ... tags = {...} WHERE id = id
            if(columns[0] != null) {
                ps.setString(bindVariableCounter++, blog.getTitle());
            }
            if(columns[1] != null) {
                ps.setString(bindVariableCounter++, blog.getContent());
            }
            if(columns[2] != null) {
                ps.setString(bindVariableCounter++, blog.getCategory());
            }
            if(columns[3] != null) {
                Array blogArray = con.createArrayOf("text", blog.getTags());
                ps.setArray(bindVariableCounter++, blogArray);
            }

            return ps;
        }, keyHolder);

        // update updated_at and updatedAt
        blog.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        jdbcTemplate.update("UPDATE posts SET updated_at = ? WHERE id = " + id, blog.getUpdatedAt());

        // set the remaining returned blog to details to match the original
        Map<String, Object> keys = keyHolder.getKeys();
        blog.setId((int) keys.get("id"));
        blog.setContent((String) keys.get("content"));
        blog.setCategory((String) keys.get("category"));

        Timestamp timestamp = (Timestamp) keys.get("created_at");
        blog.setCreatedAt(timestamp.toInstant().atOffset(ZoneOffset.UTC));

        Array rawTags = (Array) keys.get("tags");
        try {
            blog.setTags((String[]) rawTags.getArray());
        } catch (SQLException e) {
            logger.info("Could not convert Array Object to String[]");
        }

        return blog;
    }


}
