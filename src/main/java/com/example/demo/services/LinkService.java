package com.example.demo.services;

import com.example.demo.dto.LinkPostDTO;
import com.example.demo.dto.response.LinkByIdResponseDTO;
import com.example.demo.dto.response.LinkSaveResponseDTO;
import com.example.demo.dto.response.StatusByCodeResponseDTO;
import com.example.demo.entities.Link;
import com.example.demo.exception.ExpiredExceptionHandler;
import com.example.demo.exception.GenerateCodeException;
import com.example.demo.exception.InvalidKeyException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.LinkMapper;
import com.example.demo.repository.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class LinkService {

    @Autowired
    private LinkRepository repository;

    @Autowired
    private CryptographyService cryptographyService;

    // variables to generate the code
    private final String CHARS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private final int TOTAL_ATTEMPTS = 5;
    private final int CODE_size = 8;

    public LinkSaveResponseDTO save(LinkPostDTO link) {
        String secretKey = link.getSecretKey();
        validateSecretKeySize(secretKey);

        String hashSecretKey = cryptographyService.generateHash(secretKey);
        validateSecretKeyHash(hashSecretKey);

        Link linkToSave = LinkMapper.INSTANCE.linkDTOLink(link);
        linkToSave.setCode(generateCode());
        linkToSave.setExpires(LocalDateTime.now().plusDays(2));
        linkToSave.setSecretKey(hashSecretKey);
        linkToSave.setCryptoMessage(cryptographyService.encodeMessage(link.getCryptoMessage(), secretKey));

        Link saved = repository.save(linkToSave);

        return toLinkSaveResponse(saved);
    }

    @Cacheable(value = "links", key = "#id")
    public LinkByIdResponseDTO getLinkById(Long id) {
        System.out.println("======> Searching the database ID: " + id + " <======");

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

    public String getMessage(String code, String key) {
        validateSecretKeySize(key);

        Link link = getLinkByCode(code);
        validateLink(link);

        return cryptographyService.decodeMessage(link.getCryptoMessage(), key);
    }

    private Link getLinkByCode(String code) {
        Optional<Link> link = repository.findByCode(code);
        if (link.isEmpty()) {
            throw new ResourceNotFoundException("Link not found with code: " + code);
        }
        return link.get();
    }

    public void deleteExpiredLinks() {
        repository.deleteExpiredLinks(LocalDateTime.now());
    }

    public String generateCode() {
        int attempts = 0;

        while(attempts < TOTAL_ATTEMPTS) {
            Random random = new Random();
            StringBuilder sb = new StringBuilder(CODE_size);

            for (int i = 0; i < CODE_size; i++) {
                int index = random.nextInt(CHARS.length());
                sb.append(CHARS.charAt(index));
            }

            String generatedCode = sb.toString();
            Optional<Link> linkByCode = repository.findByCode(generatedCode);

            if(linkByCode.isEmpty()) {
                return generatedCode;
            }

            attempts++;
        }

       throw new GenerateCodeException("Unable to generate a code");
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

    private void validateLink(Link link) {
        if (isExpired(link.getExpires())) {
            throw new ExpiredExceptionHandler("Link expired for code: " + link.getCode());
        }
    }

    private void validateSecretKeySize(String secretKey) {
        int length = secretKey.length();

        if (length != 16 && length != 24 && length != 32) {
            throw new InvalidKeyException("Invalid key size. The key must be 16, 24, or 32 character.");
        }
    }

    private void validateSecretKeyHash(String hashSecretKey) {
        if (repository.findBySecretKey(hashSecretKey).isPresent()) {
            throw new InvalidKeyException("Invalid key size. The key must be unique.");
        }
    }

}
