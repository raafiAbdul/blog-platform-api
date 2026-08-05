package com.example.blogging_platform_api.models;

import java.time.LocalDateTime;

public class Blog {
    private String title;
    private String content;
    private String category;
    private String[] tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
