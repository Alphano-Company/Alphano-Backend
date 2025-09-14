package com.alphano.alphano.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    // private final JwtAuthorizationFilter jwtAuthorizationFilter;
    // private final JwtExceptionFilter jwtExceptionFilter;

    private static final String[] PERMIT_ALL = {
            "/", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html",
            "/h2-console/**",
            "/auth/**"
    };

    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        http
                // 세션/기본 인증 비활성화
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .logout(logout -> logout.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 경로별 인가
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PERMIT_ALL).permitAll()
                        .requestMatchers("/problems/**").permitAll()
                        .anyRequest().authenticated()
                )

                // 필터 추가
                // .addFilter(jwtExceptionFilter, UsernamePasswordAuthenticationFilter.class)
                // .addFilter(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }
}
