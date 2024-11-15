package com.acmeplex.movieticketreservation.config;

import com.acmeplex.movieticketreservation.filter.RequestLoggingFilter;
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
}
