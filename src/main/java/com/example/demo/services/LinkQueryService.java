package com.example.demo.services;

import com.example.demo.entities.Link;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class LinkQueryService {

    @Autowired
    private LinkRepository repository;

    @Cacheable(value = "linksByCode", key = "#code")
    public Link getLinkByCode(String code) {
        return repository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Link not found with code: " + code));
    }
}
