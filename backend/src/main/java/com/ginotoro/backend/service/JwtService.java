package com.ginotoro.backend.service;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import com.ginotoro.backend.config.ApplicationProperties;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class JwtService {

    @Autowired
    ApplicationProperties properties;

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(properties.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * アクセストークンの生成
     *
     * @param username
     * @return アクセストークン
     */
    public String generateAccessToken(String email) {
        String accessToken = Jwts
                .builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + properties.getAccessExpiration()))
                .signWith(getSecretKey())
                .compact();

        return accessToken;

    }

    public void setRefreshToken(String email, HttpServletResponse res) {
        String refreshToken = generateRefreshToken(email);
        // Refrese Tokenを Cookieに保存(httpOnly)
        ResponseCookie cookie = ResponseCookie
                .from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(Boolean.valueOf(properties.getCookieSecure()))
                .sameSite(properties.getCookieSameSite())
                .path("/")
                .maxAge(properties.getRefreshExpiration())
                .build();
        res.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    /**
     * リフレッシュトークン生成
     *
     * @param username
     * @return リフレッシュトークン
     */
    public String generateRefreshToken(String email) {
        String refreshToken = Jwts
                .builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + properties.getRefreshExpiration()))
                .signWith(getSecretKey())
                .compact();

        return refreshToken;
    }

    /**
     * メアド返却
     *
     * @param token
     * @return username
     */
    public String getEmail(String token) {
        return Jwts
                .parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

    }

}
