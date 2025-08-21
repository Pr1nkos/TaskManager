package ru.pr1nkos.taskmanager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.pr1nkos.taskmanager.annotation.Sanitized;

public record CreateTaskRequest(
        @NotNull(message = "Project ID cannot be null")
        Long projectId,

        @NotBlank(message = "Title cannot be empty")
        @Size(max = 100, message = "Title must be less than 100 characters")
        @Sanitized
        String title,

        @Size(max = 500, message = "Description must be less than 500 characters")
        @Sanitized
        String description
) {}
