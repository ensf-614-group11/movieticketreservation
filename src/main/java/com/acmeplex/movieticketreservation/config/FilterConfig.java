package com.acmeplex.movieticketreservation.config;

import com.acmeplex.movieticketreservation.filter.RequestLoggingFilter;
import com.acmeplex.movieticketreservation.filter.CustomHeaderFilter;
import com.acmeplex.movieticketreservation.filter.RateLimitingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration class to register custom filters in the Spring Boot application.
 * Filters are registered with a specific order, ensuring they are applied in the desired sequence.
 */
@Configuration
public class FilterConfig {

    // Logger for this configuration class
    private static final Logger logger = LoggerFactory.getLogger(FilterConfig.class);

    /**
     * Registers the RequestLoggingFilter to log details of incoming HTTP requests.
     * The filter is applied first in the chain.
     *
     * @return FilterRegistrationBean for logging filter
     */
    @Bean
    public FilterRegistrationBean<RequestLoggingFilter> loggingFilter() {
        try {
            FilterRegistrationBean<RequestLoggingFilter> registrationBean = new FilterRegistrationBean<>();
            registrationBean.setFilter(new RequestLoggingFilter());
            registrationBean.setOrder(1); // Order in which this filter will be applied
            logger.info("RequestLoggingFilter has been registered with order 1");
            return registrationBean;
        } catch (Exception e) {
            logger.error("Error occurred while registering RequestLoggingFilter", e);
            throw e; // Rethrow exception for higher-level handling
        }
    }

    /**
     * Registers the CustomHeaderFilter to add a custom header to the response.
     * The filter is applied second in the chain.
     *
     * @return FilterRegistrationBean for custom header filter
     */
    @Bean
    public FilterRegistrationBean<CustomHeaderFilter> headerFilter() {
        try {
            FilterRegistrationBean<CustomHeaderFilter> registrationBean = new FilterRegistrationBean<>();
            registrationBean.setFilter(new CustomHeaderFilter());
            registrationBean.setOrder(2); // Order in which this filter will be applied
            logger.info("CustomHeaderFilter has been registered with order 2");
            return registrationBean;
        } catch (Exception e) {
            logger.error("Error occurred while registering CustomHeaderFilter", e);
            throw e; // Rethrow exception for higher-level handling
        }
    }

    /**
     * Registers the RateLimitingFilter to limit the number of requests from the same IP address.
     * The filter is applied third in the chain.
     *
     * @return FilterRegistrationBean for rate limiting filter
     */
    @Bean
    public FilterRegistrationBean<RateLimitingFilter> rateLimitingFilter() {
        try {
            FilterRegistrationBean<RateLimitingFilter> registrationBean = new FilterRegistrationBean<>();
            registrationBean.setFilter(new RateLimitingFilter());
            registrationBean.setOrder(3); // Order in which this filter will be applied
            logger.info("RateLimitingFilter has been registered with order 3");
            return registrationBean;
        } catch (Exception e) {
            logger.error("Error occurred while registering RateLimitingFilter", e);
            throw e; // Rethrow exception for higher-level handling
        }
    }
}
