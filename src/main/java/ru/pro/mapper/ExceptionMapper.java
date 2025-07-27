package ru.pro.mapper;

import org.mapstruct.Mapper;
import ru.pro.exception.ApiException;
import ru.pro.exception.answers.ErrorResponse;

@Mapper(componentModel = "spring")
public interface ExceptionMapper {
    ErrorResponse toErrorResponse(ApiException ex);

    default ApiException toApiException(ErrorResponse response) {
        return new ApiException(
                response.statusCode(),
                response.code(),
                response.message(),
                response.details()
        );
    }
}
