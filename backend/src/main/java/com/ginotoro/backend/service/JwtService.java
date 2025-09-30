package com.ginotoro.backend.service;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginotoro.backend.config.ApplicationProperties;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

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
     * 有効なトークンか
     * DBアクセスは、冗長かつパフォーマンスロス発生する恐れあり
     * 7日内であれば、ユーザステータス変更に伴うログイン拒否も不要と判断
     *
     * @param token
     * @return 有効か
     */
    public boolean isTokenValid(String token) {

        try {
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            // 期限切れ
            System.out.println(e);
            System.out.println(e.getClass().getSimpleName());
            return false;
        } catch (JwtException e) {
            // 署名不正
            System.out.println(e);
            System.out.println(e.getClass().getSimpleName());
            return false;
        }
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
