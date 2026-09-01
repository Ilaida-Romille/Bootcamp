package com.pointwest.bootcamp.eventhubri.modules.auth.security;

import com.pointwest.bootcamp.eventhubri.modules.auth.service.JwtService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.GrantedAuthority;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                Claims claims = jwtService.parse(token);
                Long userId = Long.valueOf(claims.getSubject());

                // 1. Fetch user details from database or construct SecurityUser
                CustomUserDetails userDetails = userDetailsService.loadUserById(userId);

                if (!userDetails.isEnabled()) {
                    filterChain.doFilter(request, response);
                    return;
                }

                // 2. Use authorities directly from CustomUserDetails
                Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

                // 3. Set CustomUserDetails as the PRINCIPAL (1st argument)
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, // ✅ Set CustomUserDetails as principal instead of String email
                        null,
                        authorities
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}