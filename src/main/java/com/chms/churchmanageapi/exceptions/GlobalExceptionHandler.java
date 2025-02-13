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

import java.security.SignatureException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentialsException(BadCredentialsException exception) {
        logger.error("Exception occurred: The username or password is incorrect");
        return createProblemDetail(HttpStatus.UNAUTHORIZED, "The username or password is incorrect", exception);
    }

    @ExceptionHandler(AccountStatusException.class)
    public ProblemDetail handleAccountStatusException(AccountStatusException exception) {
        logger.error("Exception occurred: The account is locked");
        return createProblemDetail(HttpStatus.FORBIDDEN, "The account is locked", exception);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException(AccessDeniedException exception) {
        logger.error("Exception occurred: You are not authorized to access this resource");
        return createProblemDetail(HttpStatus.FORBIDDEN, "You are not authorized to access this resource", exception);
    }

    @ExceptionHandler(SignatureException.class)
    public ProblemDetail handleSignatureException(SignatureException exception) {
        logger.error("Exception occurred: The JWT signature is invalid");
        return createProblemDetail(HttpStatus.UNAUTHORIZED, "The JWT signature is invalid", exception);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ProblemDetail handleExpiredJwtException(ExpiredJwtException exception) {
        logger.error("Exception occurred: The JWT token has expired");
        return createProblemDetail(HttpStatus.UNAUTHORIZED, "The JWT token has expired", exception);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException(Exception exception) {
        logger.error("Exception occurred: Unknown internal server error", exception);
        return createProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown internal server error", exception);
    }

    private ProblemDetail createProblemDetail(HttpStatus status, String description, Exception exception) {
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(status, exception.getMessage());
        errorDetail.setProperty("description", description);
        return errorDetail;
    }
}
