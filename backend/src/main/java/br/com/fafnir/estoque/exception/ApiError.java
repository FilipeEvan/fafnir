package br.com.fafnir.estoque.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ApiError(

        LocalDateTime timestamp,

        Integer code,

        String status,

        String message,

        Map<String, String> errors) {

    public ApiError(Integer code, String status, String message) {
        this(LocalDateTime.now(), code, status, message, Map.of());
    }

    public ApiError(Integer code, String status, Map<String, String> errors) {
        this(LocalDateTime.now(), code, status, "", errors);
    }
}
