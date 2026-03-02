package com.example.demo.controller;

import com.example.demo.dto.LinkDTO;
import com.example.demo.dto.LinkPostDTO;
import com.example.demo.services.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @PostMapping
    public ResponseEntity saveLink(@RequestBody LinkPostDTO linkDTO) {
        return ResponseEntity.ok(linkService.save(linkDTO));
    }
}
