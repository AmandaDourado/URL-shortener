package com.example.demo.services;

import com.example.demo.dto.LinkPostDTO;
import com.example.demo.dto.response.LinkByIdResponseDTO;
import com.example.demo.dto.response.LinkSaveResponseDTO;
import com.example.demo.dto.response.StatusByCodeResponseDTO;
import com.example.demo.entities.Link;
import com.example.demo.exception.ExpiredExceptionHandler;
import com.example.demo.exception.InvalidKeySizeException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.LinkMapper;
import com.example.demo.repository.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class LinkService {

    @Autowired
    private LinkRepository repository;

    @Autowired
    private CryptographyService cryptographyService;

    public LinkSaveResponseDTO save(LinkPostDTO link) {
        validateSecretKey(link.getSecretKey());

        Link linkToSave = LinkMapper.INSTANCE.linkDTOLink(link);

        linkToSave.setCode(generateCode());
        linkToSave.setExpires(LocalDateTime.now().plusDays(2));
        linkToSave.setCryptoMessage(cryptographyService.encodeMessage(link.getCryptoMessage(), link.getSecretKey()));
        Link saved = repository.save(linkToSave);

        return toLinkSaveResponse(saved);
    }

    public LinkByIdResponseDTO getLinkById(Long id) {
        Link link = repository.findById(id).orElse(null);
        if (link == null) {
            throw new ResourceNotFoundException("Link not found with id: " + id);
        }
        return toLinkByIdResponse(link.getOriginalURL());
    }

    public String redirectToOriginalUrl(String code) {
        Link link = getLinkByCode(code);

        validateLink(link);
        increaseClicks(code);

        return link.getOriginalURL();
    }

    public StatusByCodeResponseDTO getStatusByCode(String code) {
        return toStatusByCodeResponse(getLinkByCode(code));
    }

    private Link getLinkByCode(String code) {
        Link link = repository.findByCode(code);
        if (link == null) {
            throw new ResourceNotFoundException("Link not found with code: " + code);
        }
        return link;
    }

    private String generateCode() {
        return UUID.randomUUID().toString().substring(0, 8).replace("-", "");
    }

    public void increaseClicks(String code) {
        repository.updateClicksByCode(code);
    }

    public boolean isExpired(LocalDateTime expires) {
        return expires.isBefore(LocalDateTime.now());
    }

    private LinkSaveResponseDTO toLinkSaveResponse(Link link) {
        return new LinkSaveResponseDTO(
                link.getId(),
                link.getCode(),
                link.getOriginalURL(),
                link.getExpires());
    }

    private LinkByIdResponseDTO toLinkByIdResponse(String originalURL) {
        return new LinkByIdResponseDTO(originalURL);
    }

    private StatusByCodeResponseDTO toStatusByCodeResponse(Link link) {
        return new StatusByCodeResponseDTO(
                link.getClicks(),
                link.getExpires(),
                link.getOriginalURL());
    }

    public String getMessage(String code, String key) {
        validateSecretKey(key);

        Link link = getLinkByCode(code);
        validateLink(link);
        return cryptographyService.decodeMessage(link.getCryptoMessage(), key);
    }


    private void validateLink(Link link) {
        if (isExpired(link.getExpires())) {
            throw new ExpiredExceptionHandler("Link expired for code: " + link.getCode());
        }
    }

    private void validateSecretKey(String secretKey) {
        int length = secretKey.length();

        if (length != 16 && length != 24 && length != 32) {
            throw new InvalidKeySizeException("Invalid key size. The key must be 16, 24, or 32 character.");
        }
    }

}
