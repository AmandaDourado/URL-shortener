package com.example.demo.services;

import com.example.demo.dto.LinkPostDTO;
import com.example.demo.entities.Link;
import com.example.demo.repository.LinkRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LinkService {

    @Autowired
    private LinkRepository repository;

    public String save(LinkPostDTO link) {
        Link linkToSave = convertToEntity(link);
        Link saved = repository.save(linkToSave);
        return saved.getId() + " - " + saved.getCode();
    }

    public String getLinkByID(Long id) {
        Link link = repository.findById(id).orElse(null);
        if (link == null) {
            return "Link not found";
        }
        return link.getOriginalURL();
    }

    private Link convertToEntity(LinkPostDTO linkDTO) {
        return new Link.Builder()
                .code(generateCode())
                .originalURL(linkDTO.getOriginalURL())
                .expires(linkDTO.getExpires())
                .build();
    }

    private String generateCode() {
        return UUID.randomUUID().toString().substring(0, 8).replace("-", "");
    }


}
