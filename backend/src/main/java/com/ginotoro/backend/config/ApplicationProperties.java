package com.ginotoro.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class ApplicationProperties {

    @Value("${ginotoro.security.jwt.access-expiration}")
    private Long accessExpiration;

    @Value("${ginotoro.security.jwt.refresh-expiration}")
    private Long refreshExpiration;

}
