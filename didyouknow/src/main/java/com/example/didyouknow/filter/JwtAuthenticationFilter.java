package com.example.didyouknow.filter;

import com.example.didyouknow.auth.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.replace("Bearer ", "");
            System.out.println("JWT Token: " + token.substring(0, Math.min(50, token.length())) + "...");

            if (jwtProvider.validateToken(token)) {
                System.out.println("Token is valid");
                Long userId = jwtProvider.getUserIdFromToken(token);
                System.out.println("Extracted userId: " + userId);

                if (userId != null) {
                    // 스프링 인증 객체 생성
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userId, null, List.of(new SimpleGrantedAuthority("ROLE_USER"))
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("Authentication set for userId: " + userId);
                } else {
                    System.out.println("ERROR: userId is null after extraction");
                }
            } else {
                System.out.println("Token is invalid");
            }
        } else {
            System.out.println("No Authorization header or doesn't start with Bearer");
        }

        filterChain.doFilter(request, response);
    }
}

