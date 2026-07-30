package com.lucianozimermann.desafiovotacaofullstack.exception;

import com.lucianozimermann.desafiovotacaofullstack.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {

        List<String> errors = e.getBindingResult()
                               .getFieldErrors()
                               .stream()
                               .map(error -> error.getDefaultMessage())
                               .toList();

        return buildErrorResponse("Erro de validação", errors, request, HttpStatus.BAD_REQUEST );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e, HttpServletRequest request) {
        return buildErrorResponse(e.getMessage(), null, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuleConflictException.class)
    public ResponseEntity<ErrorResponse> handleRuleConflictException(RuleConflictException e, HttpServletRequest request) {
        return buildErrorResponse(e.getMessage(), null, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
        return buildErrorResponse("Erro interno do servidor", null, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, List<String> errors, HttpServletRequest request, HttpStatus status) {
        ErrorResponse errorResponse = createErrorResponse(message, errors, request, status);

        return ResponseEntity.status(status).body(errorResponse);
    }

    private ErrorResponse createErrorResponse(String message, List<String> errors, HttpServletRequest request, HttpStatus status) {
        return ErrorResponse.builder()
                            .timestamp(Instant.now())
                            .status(status.value())
                            .error(message)
                            .errors(errors)
                            .path(request.getRequestURI())
                            .build();
    }
}
