package com.gateway;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(org.springframework.security.config.web.server.ServerHttpSecurity http) {
        return http.csrf(csrf -> csrf.disable())
                   .authorizeExchange(exchanges -> exchanges.anyExchange().permitAll())
                   .build();
    }
}
