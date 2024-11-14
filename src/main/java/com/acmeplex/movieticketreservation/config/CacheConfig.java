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
 * It enables Spring's caching mechanism and defines the cache manager to be used.
 * Caching allows to store results of expensive method calls to improve performance.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    // Logger for this configuration class
    private static final Logger logger = LoggerFactory.getLogger(CacheConfig.class);

    /**
     * Creates a {@link CacheManager} bean that manages caching for user profile data.
     * This configuration uses a simple in-memory cache manager with a single cache named "userProfileCache".
     *
     * @return a {@link CacheManager} configured for caching user profile data
     */
    @Bean
    public CacheManager cacheManager() {
        try {
            // Log cache manager initialization
            logger.info("Initializing CacheManager with userProfileCache");

            CacheManager cacheManager = new ConcurrentMapCacheManager("userProfileCache");

            // Log success
            logger.info("CacheManager initialized successfully with cache: userProfileCache");

            return cacheManager;
        } catch (Exception e) {
            // Log any error that occurs during cache manager creation
            logger.error("Error occurred while initializing CacheManager", e);
            throw e; // Rethrow exception for higher-level handling
        }
    }
}
