package ru.pr1nkos.taskmanager.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(
        @NotEmpty String username,
        @NotEmpty String password) {
}
