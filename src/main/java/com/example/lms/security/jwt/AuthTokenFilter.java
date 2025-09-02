package com.example.lms.security.jwt;

import com.example.lms.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("AuthTokenFilter: --- START Processing Request: " + request.getRequestURI() + " ---"); // New log
        try {
            String jwt = parseJwt(request);
            System.out.println("AuthTokenFilter: JWT found in request for URL " + request.getRequestURI() + ": " + (jwt != null ? "YES" : "NO"));

            if (jwt != null) { // Check if JWT is even present before validating
                boolean isValid = jwtTokenProvider.validateJwtToken(jwt); // <<< Validation call
                System.out.println("AuthTokenFilter: JWT validity for URL " + request.getRequestURI() + ": " + isValid); // New log

                if (isValid) {
                    String username = jwtTokenProvider.getUserNameFromJwtToken(jwt);
                    System.out.println("AuthTokenFilter: Valid JWT found for user: " + username + " for URL: " + request.getRequestURI());

                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("AuthTokenFilter: User authenticated successfully: " + username + " for URL: " + request.getRequestURI());
                } else {
                    System.out.println("AuthTokenFilter: JWT exists for URL " + request.getRequestURI() + " but is INVALID or expired. Denying access.");
                }
            } else {
                System.out.println("AuthTokenFilter: No JWT found in request for URL " + request.getRequestURI() + ". Denying access for protected resources.");
            }
        } catch (Exception e) {
            logger.error("AuthTokenFilter: Cannot set user authentication for URL {}: {}", request.getRequestURI(), e.getMessage());
            System.out.println("AuthTokenFilter: Exception during authentication for URL " + request.getRequestURI() + ": " + e.getClass().getName() + " - " + e.getMessage());
        }
        System.out.println("AuthTokenFilter: --- END Processing Request: " + request.getRequestURI() + " ---"); // New log

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}