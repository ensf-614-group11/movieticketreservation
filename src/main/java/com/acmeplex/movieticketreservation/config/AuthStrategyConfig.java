package com.acmeplex.movieticketreservation.config;

import com.acmeplex.movieticketreservation.auth.strategy.AuthStrategy;
import com.acmeplex.movieticketreservation.auth.strategy.DefaultLoginStrategy;
import com.acmeplex.movieticketreservation.auth.strategy.DefaultRegistrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AuthStrategyConfig {
    @Bean
    public Map<String, AuthStrategy<?>> authStrategies(
            DefaultLoginStrategy defaultLoginStrategy,
            DefaultRegistrationStrategy defaultRegistrationStrategy) {
        Map<String, AuthStrategy<?>> strategies = new HashMap<>();
        strategies.put("defaultLogin", defaultLoginStrategy);
        strategies.put("defaultRegistration", defaultRegistrationStrategy);
        return strategies;
    }
}
