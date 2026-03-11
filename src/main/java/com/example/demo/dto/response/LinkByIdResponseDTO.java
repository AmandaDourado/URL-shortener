package com.example.demo.dto.response;

public class LinkByIdResponseDTO {

    private String link;

    public LinkByIdResponseDTO(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
