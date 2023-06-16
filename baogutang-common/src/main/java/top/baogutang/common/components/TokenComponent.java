package top.baogutang.common.components;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import top.baogutang.common.domain.JwtBody;
import top.baogutang.common.domain.TokenCodeEnum;
import top.baogutang.common.exceptions.BusinessException;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static top.baogutang.common.constants.JwtConstant.ALGORITHM_FAMILY_NAME;

/**
 * @description:
 * @author: nikooh
 * @date: 2023/06/15 : 11:45
 */
@Slf4j
@Component
public class TokenComponent {
    @Value("${jwt.publicKey:1}")
    private String publicKey;

    public JwtBody parseToken(String token) {
        log.info("parse token:{}", token);
        Jws<Claims> claimsJws;
        try {
            claimsJws = Jwts.parser().setSigningKey(publicKeyFromBase64()).parseClaimsJws(token);
        } catch (ExpiredJwtException expiredJwtException) {
            return new JwtBody(expiredJwtException.getClaims());

        } catch (JwtException e) {
            if (e.getMessage().contains("expired")) {
                throw new BusinessException(
                        TokenCodeEnum.AUTH_TIME_OUT.getCode(), TokenCodeEnum.AUTH_TIME_OUT.getMessage());
            }
            throw new BusinessException(
                    TokenCodeEnum.AUTH_FAILED.getCode(), TokenCodeEnum.AUTH_FAILED.getMessage());
        }
        return new JwtBody(claimsJws.getBody());
    }

    public static String createToken(String data, long secondTime, String subject) {
        JwtBuilder jwtBuilder = Jwts.builder();
        subject = subject != null ? subject : UUID.randomUUID().toString();
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotEmpty(data)) {
            map.put("data", data);
        }
        jwtBuilder.setClaims(map).setSubject(subject);
        long currentTime = System.currentTimeMillis();
        if (secondTime == 0) {
            currentTime += 30 * 60 * 1000;
        } else {
            currentTime += secondTime * 1000;
        }
        Date newDate = new Date(currentTime);
        jwtBuilder.setExpiration(newDate);
        return jwtBuilder.signWith(SignatureAlgorithm.ES256, privateKeyFromBase64()).compact();
    }

    private static PrivateKey privateKeyFromBase64() {
        try {
            byte[] keyBytes = Base64Utils.decodeFromString("MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgn9N3dA834ctlm7jkBXcRvR4+/hnYjZvYO1s5hisWG0yhRANCAAR91zvPpA/9Mc4DtxAWwWsnhj1rGk0XmTNqdnfPQJmLazYXaUvEuYuR+SNf6pl0OH2jLoAsXUFYtIFUzV/qv9ph");

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_FAMILY_NAME);
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 公钥64位序列化
     *
     * @return PublicKey
     */
    private PublicKey publicKeyFromBase64() {
        try {
            byte[] keyBytes = Base64Utils.decodeFromString(publicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_FAMILY_NAME);
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}
