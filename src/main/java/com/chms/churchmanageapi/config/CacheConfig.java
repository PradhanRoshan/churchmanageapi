package com.chms.churchmanageapi.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.cache.transaction.TransactionAwareCacheManagerProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Configuration
public class CacheConfig {

    /**
     * Aggregated admin/dashboard view built in {@code MemberServiceImpl#getRegistrationTracking()}.
     *
     * This cache is intentionally short-lived to reduce staleness and because it can be memory-heavy.
     */
    public static final String CACHE_REGISTRATION_TRACKING_ALL = "registrationTrackingAll";

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager mgr = new SimpleCacheManager();

        CaffeineCache registrationTrackingAll = new CaffeineCache(
                CACHE_REGISTRATION_TRACKING_ALL,
                Caffeine.newBuilder()
                        // Short TTL: this view changes as admins review applications or members update profiles.
                        .expireAfterWrite(Duration.ofSeconds(30))
                        // Small: single key (no parameters) + keep only a few generations.
                        .maximumSize(5)
                        // Enables Micrometer cache metrics if actuator + micrometer are present.
                        .recordStats()
                        .build()
        );

        mgr.setCaches(List.of(registrationTrackingAll));
        mgr.initializeCaches();

        // Ensure cache put/evict operations participate in Spring-managed transactions
        // and are applied after a successful commit.
        return new TransactionAwareCacheManagerProxy(mgr);
    }
}
