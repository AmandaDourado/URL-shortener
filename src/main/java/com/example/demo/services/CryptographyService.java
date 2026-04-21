package com.example.demo.services;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class CryptographyService {

    private final String ALGORITHM = "AES";

    public String encodeMessage(String cryptoMessage, String secretKey) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
            Cipher cipher =  Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            return Base64.getEncoder()
                    .encodeToString(cipher.doFinal(cryptoMessage.getBytes()));

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return "";
        }
    }

    public String decodeMessage(String cryptoMessage, String secretKey) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            byte[] decodedBytes = Base64.getDecoder().decode(cryptoMessage);
            byte[] originalMessage = cipher.doFinal(decodedBytes);

            return new String(originalMessage);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
}
