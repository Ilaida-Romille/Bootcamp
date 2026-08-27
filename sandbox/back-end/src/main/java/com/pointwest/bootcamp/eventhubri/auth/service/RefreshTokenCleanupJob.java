package com.pointwest.bootcamp.eventhubri.auth.service;

import java.time.Clock;
import java.time.LocalDateTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.pointwest.bootcamp.eventhubri.identity.repository.RefreshTokenRepository;

@Component
public class RefreshTokenCleanupJob {
    private final RefreshTokenRepository refreshTokenRepository;
    private final Clock clock;

    public RefreshTokenCleanupJob(
            RefreshTokenRepository refreshTokenRepository,
            Clock clock) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.clock = clock;
    }

    @Transactional
    @Scheduled(cron = "${app.security.refresh-token-cleanup-cron:0 0 */6 * * *}")
    public void deleteExpiredRefreshTokens() {
        LocalDateTime cutoff =
            LocalDateTime.ofInstant(clock.instant(), clock.getZone());
        refreshTokenRepository.deleteExpired(cutoff);
    }
}
