package com.alphano.alphano.common.config;

import com.alphano.alphano.security.adaptor.ErrorResponseWriter;
import com.alphano.alphano.security.exception.SecurityErrorCode;
import com.alphano.alphano.security.filter.JwtAuthorizationFilter;
import com.alphano.alphano.security.filter.JwtExceptionFilter;
import com.alphano.alphano.security.jwt.JwtProvider;
import com.alphano.alphano.security.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
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
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService userDetailsService;
    private final ErrorResponseWriter writer;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationEntryPoint restEntryPoint() {
        return (request, response, auth) ->
            writer.write(response, SecurityErrorCode.AUTHENTICATION_REQUIRED);
    }

    @Bean
    public AccessDeniedHandler restDeniedHandler() {
        return (request, response, auth) ->
                writer.write(response, SecurityErrorCode.ACCESS_DENIED);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        var jwtAuthorizationFilter = new JwtAuthorizationFilter(jwtProvider, userDetailsService);
        var jwtExceptionFilter = new JwtExceptionFilter(writer);

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
                .addFilterBefore(jwtExceptionFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
