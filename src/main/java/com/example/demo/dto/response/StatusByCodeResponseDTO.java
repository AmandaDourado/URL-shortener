package com.example.demo.dto.response;

import java.time.LocalDateTime;

public class StatusByCodeResponseDTO {

    private int clicks;
    private LocalDateTime expires;
    private String url;

    public StatusByCodeResponseDTO(int clicks, LocalDateTime expires, String url) {
        this.clicks = clicks;
        this.expires = expires;
        this.url = url;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
