package com.pointwest.bootcamp.eventhubri.security.config;

import java.time.Duration;
import java.util.Base64;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.security.jwt")
@Validated
public class JwtProperties {
    @NotBlank
    private String secretBase64;
    @NotBlank
    private String issuer = "eventhubri";
    @Positive
    private long accessTokenMinutes = 15;
    @Positive
    private long refreshTokenDays = 7;

    public byte[] secretBytes() {
        return Base64.getDecoder().decode(secretBase64);
    }
    public String getSecretBase64() { return secretBase64; }
    public void setSecretBase64(String secretBase64) { this.secretBase64 = secretBase64; }
    public String getIssuer() { return issuer; }
    public void setIssuer(String issuer) { this.issuer = issuer; }
    public long getAccessTokenMinutes() { return accessTokenMinutes; }
    public void setAccessTokenMinutes(long accessTokenMinutes) { this.accessTokenMinutes = accessTokenMinutes; }
    public long getRefreshTokenDays() { return refreshTokenDays; }
    public void setRefreshTokenDays(long refreshTokenDays) { this.refreshTokenDays = refreshTokenDays; }
    public Duration accessTokenTtl() { return Duration.ofMinutes(accessTokenMinutes); }
    public Duration refreshTokenTtl() { return Duration.ofDays(refreshTokenDays); }
}
