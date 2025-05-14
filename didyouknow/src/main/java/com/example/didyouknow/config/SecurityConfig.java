package com.example.didyouknow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 비활성화 (최신 방식)
                .csrf(csrf -> csrf.disable())
                // 모든 API 요청 허용
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**", "/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                // 기본 로그인 비활성화
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());

        return http.build();
    }
}