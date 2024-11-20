package com.acmeplex.movieticketreservation.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration class to enable and configure caching for the application.
 * This uses an in-memory cache manager for performance optimization.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    // Logger for this configuration class
    private static final Logger logger = LoggerFactory.getLogger(CacheConfig.class);

    /**
     * Configures a {@link CacheManager} bean with an in-memory cache.
     * Caches user profile data to improve performance for repeated lookups.
     *
     * @return a {@link CacheManager} configured with named caches
     */
    @Bean
    public CacheManager cacheManager() {
        logger.info("Initializing CacheManager with cache: userProfileCache");
        return new ConcurrentMapCacheManager("userProfileCache");
    }
}
