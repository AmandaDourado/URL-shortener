package com.example.demo.controller;

import com.example.demo.dto.LinkPostDTO;
import com.example.demo.dto.response.LinkByIDResponseDTO;
import com.example.demo.dto.response.LinkSaveResponseDTO;
import com.example.demo.dto.response.StatusByCodeResponseDTO;
import com.example.demo.entities.Link;
import com.example.demo.services.LinkService;
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
    public ResponseEntity<LinkSaveResponseDTO> saveLink(@RequestBody LinkPostDTO linkDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(linkService.save(linkDTO));
    }

    @GetMapping(params = "id")
    public ResponseEntity<LinkByIDResponseDTO> getLinkByID(@RequestParam("id") Long id) {
        LinkByIDResponseDTO response = linkService.getLinkByID(id);
        if(response != null) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(params = "code")
    public ResponseEntity getLinkByCode(@RequestParam("code") String code) {
        Link link = linkService.getLinkByCode(code);
        if(link == null) {
            return ResponseEntity.notFound().build();
        }

        boolean expired = linkService.isExpired(link.getExpires());
        if(expired) {
            return ResponseEntity.status(HttpStatus.GONE).build();
        }

        linkService.increaseClicks(code);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(link.getOriginalURL()))
                .build();
    }

    @GetMapping(value = "/{code}/status")
    public ResponseEntity<StatusByCodeResponseDTO> getStatusByCode(@PathVariable(value = "code") String code) {
        StatusByCodeResponseDTO response = linkService.getStatusByCode(code);
        if(response == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(response);
    }
}
