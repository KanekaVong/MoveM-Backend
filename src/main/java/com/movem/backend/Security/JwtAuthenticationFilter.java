package com.movem.backend.Security;

import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Repository.AuthRepository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private com.movem.backend.Service.AuthServices.JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        final String username;

        try {
            username = jwtService.extractUsername(jwt);
        } catch (ExpiredJwtException e) {
            logger.debug("JWT expired: {}", e.getMessage());
            request.setAttribute("auth_error", "expired");
            filterChain.doFilter(request, response);
            return;
        } catch (JwtException | IllegalArgumentException e) {
            logger.debug("JWT invalid: {}", e.getMessage());
            request.setAttribute("auth_error", "invalid");
            filterChain.doFilter(request, response);
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                // NEW: check the token's pwdChangedAt claim against the current DB value
                Optional<User> userOpt = userRepository.findByUsername(username);

                if (userOpt.isEmpty()) {
                    request.setAttribute("auth_error", "invalid");
                    filterChain.doFilter(request, response);
                    return;
                }

                User user = userOpt.get();
                String tokenPwdChangedAt = jwtService.extractPasswordChangedAtClaim(jwt);
                String currentPwdChangedAt = user.getPasswordChangedAt() != null
                        ? user.getPasswordChangedAt().toString()
                        : "null";

                if (!tokenPwdChangedAt.equals(currentPwdChangedAt)) {
                    logger.debug("Token stale — password changed since issue for user: {}", username);
                    request.setAttribute("auth_error", "expired");
                    filterChain.doFilter(request, response);
                    return;
                }

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.debug("Authenticated user: {}", username);

            } catch (Exception e) {
                logger.debug("Failed to load user details for '{}': {}", username, e.getMessage());
                request.setAttribute("auth_error", "invalid");
            }
        }

        filterChain.doFilter(request, response);
    }
}