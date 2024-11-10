package com.bestbigkk.common.utils.encryption;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
* @author: 开
* @date: 2020-03-27 14:28:58
* @describe: AES工具
*/
@Component
@Slf4j
public class AESUtils {

    private static KeyGenerator keyGen;
    private static Cipher cipher;
    private static final String algorithmStr = "AES/ECB/PKCS5Padding";

    @Value("${bbk.encryption.aesSecretKey:XUGONGKAI980827}")
    private String keyStr;

    @SneakyThrows
    @PostConstruct
    void init() {
        log.info("AES工具类密匙：{}", keyStr);
        keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        cipher = Cipher.getInstance(algorithmStr);
    }

    /**
     * 加密方法.
     */
    public String encrypt(String content) {
        byte[] encryptedBytes = null;
        Key key = new SecretKeySpec(keyStr.getBytes(), "AES");
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            encryptedBytes = cipher.doFinal(stringToBytes(content));
            return bytesToString(Base64.getEncoder().encode(encryptedBytes));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("加密出错", e);
            return null;
        }
    }

    /**
     * 解密方法.
     */
    public String decrypt(String content) {
        byte[] originBytes;

        Key key = new SecretKeySpec(keyStr.getBytes(), "AES");
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            originBytes = cipher.doFinal(Base64.getDecoder().decode(content));
            return bytesToString(originBytes);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("解密出错", e);
            return null;
        }
    }

    private byte[] stringToBytes(String str) {
        return str.getBytes();
    }

    private String bytesToString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }
}