package com.ahitech.services;

import com.ahitech.exception.AppException;
import com.ahitech.services.interfaces.EncryptionService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class EncryptionServiceImpl implements EncryptionService {

    @Value("${application.security.encryption.secret-key}")
    private String stringSecretKey;

    private SecretKeySpec secretKey;
    private IvParameterSpec ivParameterSpec;

    @PostConstruct
    private void init() {
        byte[] key = Base64.getDecoder().decode(stringSecretKey);

        if (key.length != 32) {
            throw new IllegalArgumentException("Invalid key length for AES_256");
        }

        this.secretKey = new SecretKeySpec(key, "AES");
        byte[] iv = new byte[16];
        this.ivParameterSpec = new IvParameterSpec(iv);
    }

    @Override
    public String encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception exception) {
            throw new AppException("An error occurred while encrypting data", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String decrypt(String encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes);
        } catch (Exception exception) {
            throw new AppException("An error occurred while decrypting data", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
