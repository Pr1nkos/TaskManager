package ru.pr1nkos.taskmanager.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.pr1nkos.taskmanager.dto.response.ErrorResponse;


public final class ErrorResponseUtils {

    private ErrorResponseUtils() {}

    public static ResponseEntity<Object> buildErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ErrorResponse(message));
    }

    public static ResponseEntity<Object> notFound(String message) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, message);
    }

    public static ResponseEntity<Object> forbidden(String message) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, message);
    }

    public static ResponseEntity<Object> badRequest(String message) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, message);
    }

    public static ResponseEntity<Object> internalServerError(String message) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
