package ru.pr1nkos.taskmanager.exception;

public class UnauthorizedProjectAccessException extends RuntimeException {
    public UnauthorizedProjectAccessException(String message) {
        super(message);
    }
}
