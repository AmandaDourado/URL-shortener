package com.example.demo.services;

import com.example.demo.dto.LinkDTO;
import com.example.demo.entities.Link;
import com.example.demo.repository.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LinkService {

    @Autowired
    private LinkRepository repository;

    public Long save(LinkDTO link) {
        Link linkToSave = convertToEntity(link);
        Link saved = repository.save(linkToSave);
        return saved.getId();
    }

    private Link convertToEntity(LinkDTO linkDTO) {
        return new Link.Builder()
                .code(linkDTO.getCode())
                .originalURL(linkDTO.getOriginalURL())
                .clicks(linkDTO.getClicks())
                .expires(linkDTO.getExpires())
                .build();
    }
}
