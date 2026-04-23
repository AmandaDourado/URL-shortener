package com.example.demo;

import com.example.demo.dto.LinkPostDTO;
import com.example.demo.dto.response.LinkByIdResponseDTO;
import com.example.demo.dto.response.LinkSaveResponseDTO;
import com.example.demo.dto.response.StatusByCodeResponseDTO;
import com.example.demo.entities.Link;
import com.example.demo.exception.ExpiredExceptionHandler;
import com.example.demo.exception.GenerateCodeException;
import com.example.demo.exception.InvalidKeyException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.LinkRepository;
import com.example.demo.services.CryptographyService;
import com.example.demo.services.LinkService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LinkServiceTest {

    @Mock
    private LinkRepository repository;

    @Mock
    private CryptographyService cryptographyService;

    @InjectMocks
    private LinkService linkService;

    @Test
    public void shouldGenerateCode() {
        when(repository.findByCode(anyString())).thenReturn(null);

        String code = linkService.generateCode();

        assertNotNull(code);
        assertEquals(8, code.length());
    }

    @Test
    public void shouldExceptionWhenGenerateCode() {
        when(repository.findByCode(anyString())).thenReturn(new Link());

        assertThrows(GenerateCodeException.class, () -> {
            linkService.generateCode();
        });
    }

    @Test
    public void shouldSaveLink() {
        LinkPostDTO link = new LinkPostDTO();
        link.setOriginalURL("https://example.com");
        link.setCryptoMessage("secret message");
        link.setSecretKey("1234567890123456");

        when(cryptographyService.generateHash(anyString())).thenReturn("hashedKey");
        when(cryptographyService.encodeMessage(anyString(), anyString())).thenReturn("encodedMessage");
        when(repository.findByCode(anyString())).thenReturn(null);
        when(repository.findBySecretKey(anyString())).thenReturn(null);
        
        Link savedLink = new Link();
        savedLink.setId(1L);
        savedLink.setCode("abc12345");
        savedLink.setOriginalURL("https://example.com");
        savedLink.setExpires(java.time.LocalDateTime.now().plusDays(2));
        
        when(repository.save(any(Link.class))).thenReturn(savedLink);

        LinkSaveResponseDTO result = linkService.save(link);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("abc12345", result.getCode());
        assertEquals("https://example.com", result.getOriginalURL());
        assertNotNull(result.getExpires());
    }

    @Test
    public void shouldExceptionWhenSaveLink() {
        LinkPostDTO link = new LinkPostDTO();
        link.setSecretKey("1234");

        assertThrows(InvalidKeyException.class, () -> {
            linkService.save(link);
        });
    }

    @Test
    public void shouldGetLinkById(){
        when(repository.findById(anyLong())).thenReturn(Optional.of(new Link()));

        LinkByIdResponseDTO result = linkService.getLinkById(1L);

        assertNotNull(result);
    }

    @Test
    public void shouldExceptionWhenGetLinkById() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            linkService.getLinkById(1L);
        });
    }

    @Test
    public void shouldRedirectToOriginalUrl() {
        Link link = new Link();
        link.setOriginalURL("https://example.com");
        link.setExpires(LocalDateTime.now().plusDays(2));
        when(repository.findByCode(anyString())).thenReturn(link);
        doNothing().when(repository).updateClicksByCode(anyString());

        String result = linkService.redirectToOriginalUrl("abc12345");

        assertNotNull(result);
        assertEquals("https://example.com", result);
    }

    @Test
    public void shouldExceptionWhenRedirectToOriginalUrl() {
        Link link = new Link();
        link.setExpires(LocalDateTime.now().minusDays(2));
        when(repository.findByCode(anyString())).thenReturn(link);

        assertThrows(ExpiredExceptionHandler.class, () -> {
            linkService.redirectToOriginalUrl("123");
        });
    }
    
    @Test
    public void shouldGetStatusByCode() {
        when(repository.findByCode(anyString())).thenReturn(new Link());

        StatusByCodeResponseDTO result = linkService.getStatusByCode("abc12345");

        assertNotNull(result);
    }

    @Test
    public void shouldExceptionWhenGetStatusByCode() {
        when(repository.findByCode(anyString())).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            linkService.getStatusByCode("123");
        });
    }

    @Test
    public void shouldGetMessageByKey() {
        Link link = new Link();
        link.setCryptoMessage("encodedMessage");
        link.setExpires(LocalDateTime.now().plusDays(2));
        when(repository.findByCode(anyString())).thenReturn(link);
        when(cryptographyService.decodeMessage(anyString(), anyString())).thenReturn("decoded message");

        String result = linkService.getMessage("abc12345", "1234567890123456");

        assertNotNull(result);
        assertEquals("decoded message", result);
    }

    @Test
    public void shouldExceptionWhenGetMessageByKey() {
        assertThrows(InvalidKeyException.class, () -> {
            linkService.getMessage("abc12345", "1234");
        });
    }

}
