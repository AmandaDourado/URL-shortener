package com.example.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "links")
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String originalURL;
    private int clicks;
    private LocalDateTime expires;

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

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public LocalDateTime getExpires() {
        return expires;
    }

    public void setExpires(LocalDateTime expires) {
        this.expires = expires;
    }

    public static class Builder {
        private Long id;
        private String code;
        private String originalURL;
        private int clicks;
        private LocalDateTime expires;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder originalURL(String originalURL) {
            this.originalURL = originalURL;
            return this;
        }

        public Builder clicks(int clicks) {
            this.clicks = clicks;
            return this;
        }

        public Builder expires(LocalDateTime expires) {
            this.expires = expires;
            return this;
        }

        public Link build() {
            Link link = new Link();
            link.setId(id);
            link.setCode(code);
            link.setOriginalURL(originalURL);
            link.setClicks(clicks);
            link.setExpires(expires);
            return link;
        }
    }
}
