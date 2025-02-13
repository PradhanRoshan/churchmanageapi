package com.chms.churchmanageapi.config;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
                username = jwtUtil.extractUsername(jwt);
                logger.info("JWT Token found. Extracted username: {}", username);
            } else {
                logger.warn("No JWT Token found in the Authorization header.");
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    logger.info("Authenticated user: {}", username);
                } else {
                    logger.warn("JWT Token validation failed for user: {}", username);
                }
            }
        } catch (ExpiredJwtException e) {
            logger.error("JWT Token has expired", e);
//            handlerExceptionResolver.resolveException(request, response, null, e);
            setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "JWT expired", "The token has expired. Please log in again.");
            return;  // 🔴 Stop further processing
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature", e);
//            handlerExceptionResolver.resolveException(request, response, null, e);
            setErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Invalid JWT signature", "The token signature is invalid.");
            return;  // 🔴 Stop further processing
        } catch (Exception e) {
            logger.error("JWT processing error", e);
//            handlerExceptionResolver.resolveException(request, response, null, e);
            setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT", "The token is invalid.");
            return;  // 🔴 Stop further processing
        }

        filterChain.doFilter(request, response); // ✅ Only proceed if JWT is valid
    }

    private void setErrorResponse(HttpServletResponse response, int status, String error, String message) throws IOException {

        logger.error("JWT processing error response", response);

        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{ \"status\": \"" + status + "\",  \"error\": \"" + error + "\", \"message\": \"" + message + "\"}");
        response.getWriter().flush();
        response.getWriter().close();
    }
}












//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Autowired
//    private HandlerExceptionResolver handlerExceptionResolver;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        final String authorizationHeader = request.getHeader("Authorization");
//
//        String username = null;
//        String jwt = null;
//
//        try {
//            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//                jwt = authorizationHeader.substring(7);
//                username = jwtUtil.extractUsername(jwt);
//                logger.info("JWT Token found. Extracted username: {}", username);
//            } else {
//                logger.warn("No JWT Token found in the Authorization header.");
//            }
//
//            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
//
//                if (jwtUtil.validateToken(jwt, userDetails)) {
//                    UsernamePasswordAuthenticationToken authToken =
//                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(authToken);
//                    logger.info("Authenticated user: {}", username);
//                } else {
//                    logger.warn("JWT Token validation failed for user: {}", username);
//                }
//            }
//        } catch (ExpiredJwtException e) {
//            logger.error("JWT Token has expired", e);
//            throw e;  // 🔴 Let GlobalExceptionHandler handle it
//        } catch (SignatureException e) {
//            logger.error("Invalid JWT signature", e);
//            throw e;  // 🔴 Let GlobalExceptionHandler handle it
//        } catch (Exception e) {
//            logger.error("JWT processing error", e);
//            throw e;  // 🔴 Let GlobalExceptionHandler handle it
//        }
//
//
//        filterChain.doFilter(request, response); // ✅ Only proceed if JWT is valid
//    }
//}

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        final String authorizationHeader = request.getHeader("Authorization");
//
//
//        String username = null;
//        String jwt = null;
//
//        try {
//            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//                jwt = authorizationHeader.substring(7);
//
//                // Extract username and validate JWT
//                username = jwtUtil.extractUsername(jwt);
//
//                logger.info("JWT Token found. Extracted username: {}", username);
//            }
//            else {
//                logger.warn("No JWT Token found in the Authorization header.");
//            }
//
//
//
//            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
//
//                // Validate the token
//                if (jwtUtil.validateToken(jwt, userDetails)) {
//                    // If valid, set authentication in the context
//                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(authToken);
//
//                    logger.info("Authenticated user: {}", username);
//                }
//
//                else {
//                    logger.warn("JWT Token validation failed for user: {}", username);
//                }
//
//
//            }
//
//        } catch (Exception exception) {
//            logger.error("JWT Token has expired or is invalid", exception);
//            handlerExceptionResolver.resolveException(request, response, null, exception);
//        }
//
//
//        filterChain.doFilter(request, response);
//    }
//}
//
//
//
//
