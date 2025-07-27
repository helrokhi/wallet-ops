package ru.pro.exception.handlers;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.pro.exception.ApiException;
import ru.pro.exception.BadRequestInvalidRequestFormatException;
import ru.pro.exception.answers.ErrorResponse;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        Optional<FieldError> optionalError = Optional.of(ex)
                .map(MethodArgumentNotValidException::getBindingResult)
                .flatMap(result -> result.getFieldErrors().stream().findFirst());

        if (optionalError.isEmpty()) {
            log.warn("Ошибка валидации без FieldError", ex);
            return buildResponse(
                    BAD_REQUEST.value(),
                    "INVALID_REQUEST_FORMAT",
                    "Ошибка валидации",
                    Map.of(),
                    now()
            );
        }

        FieldError fieldError = optionalError.get();

        String field = fieldError.getField();
        String value = Optional.ofNullable(fieldError.getRejectedValue()).map(Object::toString).orElse("null");
        String expected = fieldError.getDefaultMessage();
        ApiException wrapped = new BadRequestInvalidRequestFormatException(field, value, expected);

        HttpStatus status = HttpStatus.valueOf(wrapped.getStatusCode());
        logAtLevel(status, wrapped.getCode(), ex);
        return buildResponse(
                wrapped.getStatusCode(),
                wrapped.getCode(),
                wrapped.getMessage(),
                wrapped.getDetails(),
                wrapped.getTimestamp()
        );
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException ex) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode());
        logAtLevel(status, ex.getCode(), ex);
        return buildResponse(
                ex.getStatusCode(),
                ex.getCode(),
                ex.getMessage(),
                ex.getDetails(),
                ex.getTimestamp()
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
        return handleWalletError(NOT_FOUND, "ENTITY_NOT_FOUND", ex.getMessage(), ex);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointer(NullPointerException ex) {
        return handleWalletError(NOT_FOUND, "NULL", ex.getMessage(), ex);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return handleWalletError(BAD_REQUEST, "INVALID_ARGUMENT", ex.getMessage(), ex);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJson(HttpMessageNotReadableException ex) {
        Throwable rootCause = ex.getMostSpecificCause();
        String message = rootCause.getMessage();
        return handleWalletError(BAD_REQUEST, "INVALID_JSON", message, ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOtherExceptions(Exception ex) {
        log.error("Необработанное исключение", ex);
        return buildResponse(
                INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_ERROR",
                "Произошла внутренняя ошибка. Попробуйте позже.",
                Map.of(),
                now()
        );
    }

    private ResponseEntity<ErrorResponse> handleWalletError(
            HttpStatus status, String code, String message, Throwable ex) {
        log.warn(code, ex);
        return buildResponse(status.value(), code, message, Map.of(), now());
    }

    private ResponseEntity<ErrorResponse> buildResponse(int statusCode,
                                                        String code,
                                                        String message,
                                                        Map<String, Object> details,
                                                        Timestamp timestamp) {
        ErrorResponse error = new ErrorResponse(
                statusCode,
                code,
                message,
                details,
                timestamp
        );
        return ResponseEntity.status(statusCode).body(error);
    }

    private void logAtLevel(HttpStatus status, String code, Throwable ex) {
        if (status.is4xxClientError()) {
            log.warn("{}: {}", code, ex.getMessage());
        } else {
            log.error("{}: {}", code, ex.getMessage());
        }
    }

    private Timestamp now() {
        return Timestamp.from(Instant.now());
    }
}
