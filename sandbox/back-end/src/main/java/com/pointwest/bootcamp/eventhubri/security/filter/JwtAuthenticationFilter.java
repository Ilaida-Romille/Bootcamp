package com.pointwest.bootcamp.eventhubri.security.filter;

import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.pointwest.bootcamp.eventhubri.identity.entity.User;
import com.pointwest.bootcamp.eventhubri.identity.repository.UserRepository;
import com.pointwest.bootcamp.eventhubri.security.jwt.JwtTokenProvider;
import com.pointwest.bootcamp.eventhubri.security.service.UserAuthorityProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final UserAuthorityProvider authorityProvider;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    public JwtAuthenticationFilter(
            JwtTokenProvider tokenProvider,
            UserRepository userRepository,
            UserAuthorityProvider authorityProvider,
            AuthenticationEntryPoint authenticationEntryPoint) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.authorityProvider = authorityProvider;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        return path.equals("/api/auth/login")
                || path.equals("/api/auth/refresh")
                || path.equals("/api/auth/logout");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header == null || header.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!header.regionMatches(true, 0, "Bearer ", 0, 7)) {
            authenticationEntryPoint.commence(request, response,
                new AuthenticationException("Unsupported Authorization scheme") {});
            return;
        }

        String token = header.substring(7).trim();
        if (token.isBlank()) {
            authenticationEntryPoint.commence(request, response,
                new AuthenticationException("Bearer token is missing") {});
            return;
        }

        try {
            Claims claims = tokenProvider.parseAndValidateAccessToken(token);
            String email = tokenProvider.getSubject(claims);
            Long userId = tokenProvider.getUserId(claims);

            if (email == null || userId == null) {
                throw new JwtException("JWT subject or user id is missing");
            }

            User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new JwtException("User no longer exists"));

            if (!userId.equals(user.getId())) {
                throw new JwtException("JWT user id does not match account");
            }

            if (!Boolean.TRUE.equals(user.getIsActive())) {
                throw new JwtException("User account is inactive");
            }

            var authentication = new UsernamePasswordAuthenticationToken(
                user, null, authorityProvider.getAuthorities(user));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (JwtException | IllegalArgumentException ex) {
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response,
                new AuthenticationException("Invalid or expired access token", ex) {});
        }
    }
}
