package com.example.demo.services;

import com.example.demo.dto.response.LinkByIdResponseDTO;
import com.example.demo.entities.Link;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LinkCacheService {

    @Autowired
    private LinkRepository repository;

    @Cacheable(value = "linksById", key = "#id")
    public Optional<Link> getLinkById(Long id) {
        return repository.findById(id);
    }

    @Cacheable(value = "linksByCode", key = "#code")
    public Link getLinkByCode(String code) {
        return repository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Link not found with code: " + code));
    }

    @Cacheable(value = "linksBySecretKey", key = "#hashSecretKey")
    public Optional<Link> linkBySecretKey(String hashSecretKey) {
        return repository.findBySecretKey(hashSecretKey);
    }
}
