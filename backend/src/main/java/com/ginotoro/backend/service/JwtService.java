package com.ginotoro.backend.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import com.ginotoro.backend.config.ApplicationProperties;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class JwtService {

    @Autowired
    ApplicationProperties properties;

    /**
     * アクセストークンを生成
     *
     * @param email
     * @return accessToken
     */
    public String generateAccessToken(String email) {
        String accessToken = Jwts
                .builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + properties.getAccessExpiration()))
                .signWith(properties.getSecretKey())
                .compact();

        return accessToken;

    }

    /**
     * HttpOnly Cookieへセットする
     *
     * @param email
     * @param res
     */
    public void setRefreshToken(String email, HttpServletResponse res) {
        String refreshToken = generateRefreshToken(email);
        // Refrese Tokenを Cookieに保存(httpOnly)
        ResponseCookie cookie = ResponseCookie
                .from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(properties.getCookieSecure())
                .sameSite(properties.getCookieSameSite())
                .path("/")
                .maxAge(properties.getRefreshExpiration())
                .build();
        res.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    /**
     * refreshToken を削除
     * Cookieの有効期限0に変更
     * 
     * @param email
     * @param res
     */
    public void clearRefreshToken(HttpServletResponse res) {
        // Refrese Tokenを Cookieに保存(httpOnly)
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();

        res.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    /**
     * リフレッシュトークン生成
     *
     * @param email
     * @return refreshToken
     */
    public String generateRefreshToken(String email) {
        String refreshToken = Jwts
                .builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + properties.getRefreshExpiration()))
                .signWith(properties.getSecretKey())
                .compact();

        return refreshToken;
    }

    /**
     * JWT からユーザーのメールアドレス (subject) を抽出
     *
     * @param token
     * @return email
     */
    public String getEmail(String token) {
        return Jwts
                .parser()
                .verifyWith(properties.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

    }

}
