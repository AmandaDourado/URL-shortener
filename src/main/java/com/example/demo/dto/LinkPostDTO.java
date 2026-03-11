package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public class LinkPostDTO {

    @NotBlank(message = "originalURL is required")
    @URL(message = "URL must be valid")
    private String originalURL;

    public String getOriginalURL() {
        return originalURL;
    }

    public void setOriginalURL(String originalURL) {
        this.originalURL = originalURL;
    }

}
