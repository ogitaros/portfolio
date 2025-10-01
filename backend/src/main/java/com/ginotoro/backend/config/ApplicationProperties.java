package com.ginotoro.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

@Component
public class ApplicationProperties {

    @Value("${ginotoro.security.jwt.access-expiration}")
    private Long accessExpiration;

    @Value("${ginotoro.security.jwt.refresh-expiration}")
    private Long refreshExpiration;

    @Value("${ginotoro.security.jwt.secret-key}")
    private String secretKeyBase64;

    @Value("${ginotoro.security.cookie.sameSite}")
    private String cookieSameSite;

    @Value("${ginotoro.security.cookie.secure}")
    private String cookieSecure;

    @Value("${ginotoro.security.uri}")
    private String uri;

    @Value("${ginotoro.environment}")
    private String environment;

    // Getter
    public Long getAccessExpiration() {
        return accessExpiration;
    }

    public Long getRefreshExpiration() {
        return refreshExpiration;
    }

    public String getSecretKeyBase64() {
        return secretKeyBase64;
    }

    public String getCookieSameSite() {
        return cookieSameSite;
    }

    public String getUri() {
        return uri;
    }

    public boolean getCookieSecure() {
        return Boolean.valueOf(cookieSecure);
    }

    private SecretKey cachedSecretKey;

    public SecretKey getSecretKey() {
        if (cachedSecretKey == null) {
            byte[] keyBytes = Decoders.BASE64.decode(secretKeyBase64);
            cachedSecretKey = Keys.hmacShaKeyFor(keyBytes);
        }
        return cachedSecretKey;
    }

    public String getEnvironment() {
        return environment;
    }

}