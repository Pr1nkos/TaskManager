package ru.pr1nkos.taskmanager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.pr1nkos.taskmanager.constant.TaskStatus;

public record UpdateTaskRequest(
        @NotBlank(message = "Title cannot be empty")
        @Size(max = 100, message = "Title must be less than 100 characters")
        String title,
        @Size(max = 500, message = "Description must be less than 500 characters")
        String description,
        TaskStatus status
) {}
