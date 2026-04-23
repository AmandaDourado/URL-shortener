package com.example.demo;

import com.example.demo.services.CryptographyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CryptographyServiceTest {

    private CryptographyService cryptographyService = new CryptographyService();

    private final String SECRET_KEY = "1234567890123456";
    private final String INVALID_KEY = "123";
    private final String MESSAGE = "HelloWorld";

    @Test
    public void shouldEncodeMessage() {
        String encoded = cryptographyService.encodeMessage(MESSAGE, SECRET_KEY);

        assertNotNull(encoded);
        assertNotEquals(MESSAGE, encoded);
    }

    @Test
    public void shouldExceptionWhenEncodeMessageWithInvalidKey() {
        String encoded = cryptographyService.encodeMessage(MESSAGE, INVALID_KEY);

        assertNull(encoded);
    }

    @Test
    public void shouldDecodeMessage() {
        String encoded = cryptographyService.encodeMessage(MESSAGE, SECRET_KEY);
        String decoded = cryptographyService.decodeMessage(encoded, SECRET_KEY);

        assertNotNull(decoded);
        assertEquals(MESSAGE, decoded);
    }

    @Test
    public void shouldExceptionWhenDecodeMessageWithInvalidKey() {
        String decoded = cryptographyService.decodeMessage(MESSAGE, INVALID_KEY);

        assertNull(decoded);
    }

    @Test
    public void shouldGenerateHash() {
        String hash = cryptographyService.generateHash(MESSAGE);

        assertNotNull(hash);
        assertNotEquals(MESSAGE, hash);
        assertEquals(44, hash.length());
    }

    @Test
    public void shouldGenerateHashConsistently() {
        String hash1 = cryptographyService.generateHash(MESSAGE);
        String hash2 = cryptographyService.generateHash(MESSAGE);

        assertEquals(hash1, hash2);
    }

}
