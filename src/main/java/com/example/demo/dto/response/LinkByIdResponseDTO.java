package com.example.demo.dto.response;

import java.io.Serializable;

public class LinkByIdResponseDTO implements Serializable {

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
