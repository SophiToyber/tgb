package io.bf.messengers.telegram.engine.utils;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CryptoAES {

    @Value("${credentials.secret}")
    public String secret;

    @PostConstruct
    private void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String decrypt(String strToDecrypt) {
        log.info("decrypt {}", strToDecrypt);
        try {
            SecretKeySpec secretKey = getSecretKeySpec(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            log.error("Error while decrypting: {}", e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private SecretKeySpec getSecretKeySpec(String myKey) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] key = Arrays.copyOf(sha.digest(myKey.getBytes("UTF-8")), 16);
            return new SecretKeySpec(key, "AES");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
