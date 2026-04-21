package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public class LinkPostDTO {

    @NotBlank(message = "originalURL is required")
    @URL(message = "URL must be valid")
    private String originalURL;
    @NotBlank(message = "cryptoMessage is required")
    private String cryptoMessage;
    @NotBlank(message = "secretKey is required")
    @Size(min = 16, max = 32, message = "The size should be between 16 and 32 characters.")
    private String secretKey;

    public String getOriginalURL() {
        return originalURL;
    }

    public void setOriginalURL(String originalURL) {
        this.originalURL = originalURL;
    }

    public String getCryptoMessage() {
        return cryptoMessage;
    }

    public void setCryptoMessage(String cryptoMessage) {
        this.cryptoMessage = cryptoMessage;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
