package com.example.burgerconstructorbackend.security;

import com.example.burgerconstructorbackend.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        String token = null;
        if (header != null && !header.isBlank()) {
            header = header.trim();
            if (header.startsWith("Bearer ")) {
                token = header.substring(7).trim();
            } else {
                token = header;
            }
        }

        if (token != null && token.isBlank()) {
            request.setAttribute("jwtError", "invalid token");
        } else if (token != null) {
            try {
                Claims claims = jwtService.extractClaims(token);
                String tokenType = claims.get("type", String.class);
                
                if (!"access".equals(tokenType)) {
                    request.setAttribute("jwtError", "invalid token");
                } else {
                    String email = claims.getSubject();

                    userRepository.findByEmail(email).ifPresentOrElse(user -> {

                        List<GrantedAuthority> authorities =
                                List.of(new SimpleGrantedAuthority("ROLE_USER"));

                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(
                                        user,
                                        null,
                                        authorities
                                );

                        auth.setDetails(
                                new WebAuthenticationDetailsSource()
                                        .buildDetails(request)
                        );

                        SecurityContextHolder
                                .getContext()
                                .setAuthentication(auth);
                    }, () -> request.setAttribute("jwtError", "invalid token"));
                }
            } catch (ExpiredJwtException e) {
                request.setAttribute("jwtError", "jwt expired");
            } catch (JwtException | IllegalArgumentException e) {
                request.setAttribute("jwtError", "invalid token");
            }
        }

        filterChain.doFilter(request, response);
    }
}

