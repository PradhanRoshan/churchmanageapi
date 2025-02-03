package com.chms.churchmanageapi.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    private static final Logger logger = LoggerFactory.getLogger(AuditorAwareImpl.class);

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            String dtoUsername = UserContextUtil.getUser(); // Get username from UserContext
            if (dtoUsername != null && !dtoUsername.isBlank()) {
                logger.info("Using username from DTO: {}", dtoUsername);
                return Optional.of(dtoUsername.toUpperCase());
            }
            logger.warn("No authenticated user or DTO username found. Using 'DEFAULT_USER' as fallback.");
            return Optional.of("DEFAULT_USER");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            logger.info("Current auditor: {}", username);
            return Optional.of(username.toUpperCase());
        } else {
            logger.info("Current auditor (non-UserDetails): {}", principal.toString());
            return Optional.of(principal.toString().toUpperCase());
        }
    }
}
