package com.chms.churchmanageapi.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.security.SignatureException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentialsException(BadCredentialsException exception) {
        logger.error("Exception occurred: The username or password is incorrect");
        return createProblemDetail(HttpStatus.UNAUTHORIZED, "The username or password is incorrect", exception.getMessage());
    }

    @ExceptionHandler(AccountStatusException.class)
    public ProblemDetail handleAccountStatusException(AccountStatusException exception) {
        logger.error("Exception occurred: The account is locked");
        return createProblemDetail(HttpStatus.FORBIDDEN, "The account is locked", exception.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException(AccessDeniedException exception) {
        logger.error("Exception occurred: You are not authorized to access this resource");
        return createProblemDetail(HttpStatus.FORBIDDEN, "You are not authorized to access this resource", exception.getMessage());
    }

    @ExceptionHandler(SignatureException.class)
    public ProblemDetail handleSignatureException(SignatureException exception) {
        logger.error("Exception occurred: The JWT signature is invalid");
        return createProblemDetail(HttpStatus.UNAUTHORIZED, "The JWT signature is invalid", exception.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ProblemDetail handleExpiredJwtException(ExpiredJwtException exception) {
        logger.error("Exception occurred: The JWT token has expired");
        return createProblemDetail(HttpStatus.UNAUTHORIZED, "The JWT token has expired", exception.getMessage());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ProblemDetail handleResponseStatusException(ResponseStatusException exception) {
        logger.error("ResponseStatusException occurred: {} - {}", exception.getStatusCode(), exception.getReason());
        // ResponseStatusException#getStatusCode() returns HttpStatusCode in newer Spring versions.
        // Resolve it to HttpStatus; if resolution fails, default to 500.
        HttpStatus status = HttpStatus.resolve(exception.getStatusCode().value());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        String detail = exception.getReason() != null ? exception.getReason() : exception.getMessage();
        return createProblemDetail(status, detail, detail);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException(Exception exception) {
        logger.error("Exception occurred: Unknown internal server error", exception);
        return createProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown internal server error", exception.getMessage());
    }

    private ProblemDetail createProblemDetail(HttpStatus status, String description, String detail) {
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(status, detail);
        errorDetail.setProperty("description", description);
        return errorDetail;
    }
}
