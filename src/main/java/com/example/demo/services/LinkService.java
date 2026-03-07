package com.example.demo.services;

import com.example.demo.dto.LinkPostDTO;
import com.example.demo.entities.Link;
import com.example.demo.repository.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
            return HttpStatusCode.valueOf(404).toString();
        }
        return link.getOriginalURL();
    }

    public String getLinkByCode(String code) {
        Link link = repository.findByCode(code);
        if(link != null) {
            increaseClicks(code);
            return isExpired(link.getExpires()) ?
                    HttpStatusCode.valueOf(410).toString() :
                    HttpStatusCode.valueOf(302).toString();
        }
        return HttpStatusCode.valueOf(404).toString();
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

    private void increaseClicks(String code) {
        repository.updateClicksByCode(code);
    }

    private boolean isExpired(LocalDateTime expires) {
        return expires.isBefore(LocalDateTime.now());
    }



}
