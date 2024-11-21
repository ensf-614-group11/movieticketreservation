package com.acmeplex.movieticketreservation.auth.context;

import com.acmeplex.movieticketreservation.auth.strategy.AuthStrategy;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AuthStrategyContext {
    private final Map<String, AuthStrategy<?>> strategyMap;

    public AuthStrategyContext(Map<String, AuthStrategy<?>> strategies) {
        this.strategyMap = strategies;
    }

    public <T> AuthStrategy<T> getStrategy(String strategyType) {
        @SuppressWarnings("unchecked")
        AuthStrategy<T> strategy = (AuthStrategy<T>) strategyMap.get(strategyType);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for type: " + strategyType);
        }
        return strategy;
    }
}
