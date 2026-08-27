package com.pointwest.bootcamp.eventhubri.security.jwt;

import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import com.pointwest.bootcamp.eventhubri.security.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {
    public static final String CLAIM_TOKEN_TYPE = "typ";
    public static final String CLAIM_USER_ID = "uid";
    public static final String CLAIM_ROLES = "roles";
    public static final String TOKEN_TYPE_ACCESS = "access";

    private final JwtProperties properties;
    private final SecretKey signingKey;
    private final Clock clock;

    public JwtTokenProvider(JwtProperties properties, Clock clock) {
        this.properties = properties;
        this.clock = clock;
        byte[] secretBytes;
        try {
            secretBytes = Decoders.BASE64.decode(properties.getSecretBase64());
        } catch (IllegalArgumentException ex) {
            throw new IllegalStateException(
                "app.security.jwt.secret-base64 must be valid Base64", ex);
        }
        if (secretBytes.length < 32) {
            throw new IllegalStateException(
                "app.security.jwt.secret-base64 must decode to at least 256 bits");
        }
        this.signingKey = Keys.hmacShaKeyFor(secretBytes);
    }

    public String generateAccessToken(Long userId, String email, List<String> roles) {
        Instant issuedAt = clock.instant();
        Instant expiresAt = issuedAt.plus(properties.accessTokenTtl());

        return Jwts.builder()
            .id(UUID.randomUUID().toString())
            .issuer(properties.getIssuer())
            .subject(email)
            .claim(CLAIM_TOKEN_TYPE, TOKEN_TYPE_ACCESS)
            .claim(CLAIM_USER_ID, userId)
            .claim(CLAIM_ROLES, roles)
            .issuedAt(Date.from(issuedAt))
            .expiration(Date.from(expiresAt))
            .signWith(signingKey, Jwts.SIG.HS256)
            .compact();
    }

    public Claims parseAndValidateAccessToken(String token) {
        if (token == null || token.isBlank()) {
            throw new JwtException("JWT is empty");
        }
        Jws<Claims> jws = Jwts.parser()
            .verifyWith(signingKey)
            .requireIssuer(properties.getIssuer())
            .require(CLAIM_TOKEN_TYPE, TOKEN_TYPE_ACCESS)
            .build()
            .parseSignedClaims(token);
        return jws.getPayload();
    }

    public long getAccessTokenTtlSeconds() {
        return properties.accessTokenTtl().toSeconds();
    }

    public Long getUserId(Claims claims) {
        Number value = claims.get(CLAIM_USER_ID, Number.class);
        return value == null ? null : value.longValue();
    }

    public String getSubject(Claims claims) {
        return claims.getSubject();
    }
}
