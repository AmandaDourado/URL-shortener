package com.example.demo.services;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class CryptographyService {

    private final String AES = "AES";
    private final String SHA_256 = "SHA-256";

    public String encodeMessage(String cryptoMessage, String secretKey) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), AES);
            Cipher cipher =  Cipher.getInstance(AES);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            return Base64.getEncoder()
                    .encodeToString(cipher.doFinal(cryptoMessage.getBytes()));

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public String decodeMessage(String cryptoMessage, String secretKey) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), AES);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            byte[] decodedBytes = Base64.getDecoder().decode(cryptoMessage);
            byte[] originalMessage = cipher.doFinal(decodedBytes);

            return new String(originalMessage);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
    public String generateHash(String message) {
        try {
            MessageDigest digest = MessageDigest.getInstance(SHA_256);
            byte[] hash = digest.digest(message.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
