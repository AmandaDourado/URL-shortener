package com.example.demo.controller;

import com.example.demo.dto.LinkPostDTO;
import com.example.demo.services.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/links")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @PostMapping
    public ResponseEntity saveLink(@RequestBody LinkPostDTO linkDTO) {
        return ResponseEntity.ok(linkService.save(linkDTO));
    }

    @GetMapping(params = "id")
    public ResponseEntity getLinkByID(@RequestParam("id") Long id) {
        return ResponseEntity.ok(linkService.getLinkByID(id));

    }
}
