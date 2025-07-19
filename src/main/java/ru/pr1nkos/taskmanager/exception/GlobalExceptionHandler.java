package ru.pr1nkos.taskmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.pr1nkos.taskmanager.dto.response.ErrorResponse;
import ru.pr1nkos.taskmanager.entity.Project;
import ru.pr1nkos.taskmanager.util.ErrorResponseUtils;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedProjectAccessException.class)
    public ResponseEntity<Object> handleUnauthorizedProjectAccessException(UnauthorizedProjectAccessException ex) {
        return ErrorResponseUtils.forbidden(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleOtherExceptions(Exception ex) {
        return ErrorResponseUtils.internalServerError("An unexpected error occurred");
    }
}
