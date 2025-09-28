package com.ginotoro.backend.service;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginotoro.backend.config.ApplicationProperties;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;

@Service
public class JwtService {
    // 本番用
    SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
    // HS256用のランダムキーを生成
    // 検証中はわかりやすいように固定キーにしたいのでコメントアウト
    // private static final byte[] keyBytes = Decoders.BASE64
    // .decode("oginoShotaroBase64SecretStringOginoShotaroBase64SecretString");
    // private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(keyBytes);

    @Autowired
    ApplicationProperties properties;

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
                .signWith(SECRET_KEY)
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
                .signWith(SECRET_KEY)
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
                    .verifyWith(SECRET_KEY)
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
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

    }

}
