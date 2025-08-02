package ru.pr1nkos.taskmanager.exception;

public class DuplicateProjectNameException extends RuntimeException {
    public DuplicateProjectNameException(String message) {
        super(message);
    }
}
