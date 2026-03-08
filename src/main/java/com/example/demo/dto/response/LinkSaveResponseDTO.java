package com.example.demo.dto.response;

import java.time.LocalDateTime;

public class LinkSaveResponseDTO {

    private Long id;
    private String code;
    private String originalURL;
    private LocalDateTime expires;

    public LinkSaveResponseDTO(Long id, String code, String originalURL, LocalDateTime expires) {
        this.id = id;
        this.code = code;
        this.originalURL = originalURL;
        this.expires = expires;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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
