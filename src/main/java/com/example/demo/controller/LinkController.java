package com.example.demo.controller;

import com.example.demo.dto.LinkPostDTO;
import com.example.demo.dto.response.LinkByIdResponseDTO;
import com.example.demo.dto.response.LinkSaveResponseDTO;
import com.example.demo.dto.response.StatusByCodeResponseDTO;
import com.example.demo.services.LinkService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/links")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @PostMapping
    public ResponseEntity<LinkSaveResponseDTO> saveLink(@Valid @RequestBody LinkPostDTO linkDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(linkService.save(linkDTO));
    }

    @GetMapping(params = "id")
    public ResponseEntity<LinkByIdResponseDTO> getLinkById(@RequestParam("id") Long id) {
        LinkByIdResponseDTO response = linkService.getLinkById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(params = "code")
    public ResponseEntity redirectToOriginalUrl(@RequestParam("code") String code) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(linkService.redirectToOriginalUrl(code)))
                .build();
    }

    @GetMapping(value = "/{code}/status")
    public ResponseEntity<StatusByCodeResponseDTO> getStatusByCode(@PathVariable(value = "code") String code) {
        return ResponseEntity.ok(linkService.getStatusByCode(code));
    }
}
