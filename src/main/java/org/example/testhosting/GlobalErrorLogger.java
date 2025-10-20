package org.example.testhosting;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalErrorLogger {

    private static final Logger log = LoggerFactory.getLogger(GlobalErrorLogger.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handle(Exception e, HttpServletRequest request) {
        log.error("Exception at {} : {}", request.getRequestURI(), e.getMessage(), e);
        return ResponseEntity.status(500).body("Internal Error");
    }
}
