
package com.example.purchase.handler;

import com.example.purchase.domain.DuplicatePurchaseRequest;
import com.example.purchase.domain.PurchaseRequestNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.HttpMediaTypeNotSupportedException;

import java.util.UUID;

@RestControllerAdvice
public class ApiExceptionHandler {

    private String getCorrelationId(HttpServletRequest request) {
        String correlationId = request.getHeader("X-Correlation-Id");

        if (correlationId == null || correlationId.isBlank()) {
            return UUID.randomUUID().toString();
        }

        return correlationId;
    }

    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status,
            String code,
            String message,
            HttpServletRequest request
    ) {
        String correlationId = getCorrelationId(request);

        return ResponseEntity.status(status)
                .header("X-Correlation-Id", correlationId)
                .body(new ErrorResponse(code, message, correlationId));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .orElse("Validation failed");

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "VALIDATION",
                message,
                request
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException exception,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "VALIDATION",
                exception.getMessage(),
                request
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJson(
            HttpMessageNotReadableException exception,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "VALIDATION",
                "body must be JSON",
                request
        );
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedMediaType(
            HttpMediaTypeNotSupportedException exception,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                "UNSUPPORTED",
                "Unsupported media type",
                request
        );
    }

    @ExceptionHandler(DuplicatePurchaseRequest.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(
            DuplicatePurchaseRequest exception,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.CONFLICT,
                "DUPLICATE",
                exception.getMessage(),
                request
        );
    }

    @ExceptionHandler(PurchaseRequestNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            PurchaseRequestNotFoundException exception,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.NOT_FOUND,
                "NOT_FOUND",
                exception.getMessage(),
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(
            Exception exception,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL",
                "unexpected error",
                request
        );
    }
}
