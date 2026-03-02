package com.example.demo.dto;

import java.time.LocalDateTime;

public class LinkPostDTO {

    private String originalURL;
    private LocalDateTime expires;

    public String getOriginalURL() {
        return originalURL;
    }

    public void setOriginalURL(String originalURL) {
        this.originalURL = originalURL;
    }

    public LocalDateTime getExpires() {
        return expires;
    }

    public void setExpires(LocalDateTime expires) {
        this.expires = expires;
    }
}
