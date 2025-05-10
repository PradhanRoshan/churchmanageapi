package com.chms.churchmanageapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            try {
                String username = jwtUtil.extractUsername(jwt);
                logger.debug("Extracted username: {}", username);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (jwtUtil.validateToken(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        logger.info("Authenticated user: {}", username);
                    } else {
                        logger.warn("JWT validation failed for user: {}", username);
                        setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                                "Invalid JWT", "The token is invalid or tampered.");
                        return;
                    }
                }
            } catch (ExpiredJwtException e) {
                logger.warn("JWT expired: {}", e.getMessage());
                setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                        "JWT Expired", "The token has expired. Please login again.");
                return;
            } catch (SignatureException e) {
                logger.error("JWT signature error: {}", e.getMessage());
                setErrorResponse(response, HttpServletResponse.SC_FORBIDDEN,
                        "Invalid Signature", "The token signature is invalid.");
                return;
            } catch (Exception e) {
                logger.error("Unexpected JWT processing error", e);
                setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                        "Invalid JWT", "An error occurred while processing the token.");
                return;
            }
        } else {
            logger.warn("Missing or invalid Authorization header");
        }

        filterChain.doFilter(request, response);
    }

    private void setErrorResponse(HttpServletResponse response, int status, String error, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponse errorResponse = new ErrorResponse(status, error, message);
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}







//package com.chms.churchmanageapi.config;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.SignatureException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//import org.springframework.web.servlet.HandlerExceptionResolver;
//
//import java.io.IOException;
//
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
////            handlerExceptionResolver.resolveException(request, response, null, e);
//            setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "JWT expired", "The token has expired. Please log in again.");
//            return;  // 🔴 Stop further processing
//        } catch (SignatureException e) {
//            logger.error("Invalid JWT signature", e);
////            handlerExceptionResolver.resolveException(request, response, null, e);
//            setErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Invalid JWT signature", "The token signature is invalid.");
//            return;  // 🔴 Stop further processing
//        } catch (Exception e) {
//            logger.error("JWT processing error", e);
////            handlerExceptionResolver.resolveException(request, response, null, e);
//            setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT", "The token is invalid.");
//            return;  // 🔴 Stop further processing
//        }
//
//        filterChain.doFilter(request, response); // ✅ Only proceed if JWT is valid
//    }
//
//    private void setErrorResponse(HttpServletResponse response, int status, String error, String message) throws IOException {
//
//        logger.error("JWT processing error response", response);
//
//        response.setStatus(status);
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        response.getWriter().write("{ \"status\": \"" + status + "\",  \"error\": \"" + error + "\", \"message\": \"" + message + "\"}");
//        response.getWriter().flush();
//        response.getWriter().close();
//    }
//}

