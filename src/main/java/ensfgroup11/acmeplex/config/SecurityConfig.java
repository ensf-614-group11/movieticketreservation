package ensfgroup11.acmeplex.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().permitAll()  // Allows access to all requests without authentication
                )
                .csrf().disable()             // Disables CSRF for non-secure applications (optional)
                .formLogin().disable()        // Disables the default login form
                .httpBasic().disable();       // Disables HTTP basic authentication

        return http.build();
    }
}
