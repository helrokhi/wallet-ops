package ru.pro.exception;

import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class BadRequestInvalidRequestFormatException extends ApiException {
    public BadRequestInvalidRequestFormatException(String field, String value, String expectedFormat) {
        super(BAD_REQUEST.value(),
                "INVALID_REQUEST_FORMAT",
                "Некорректный формат запроса",
                Map.of(
                        "field", field,
                        "value", value,
                        "expected_format", expectedFormat
                ));
    }
}
