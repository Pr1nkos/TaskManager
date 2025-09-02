package ru.pr1nkos.taskmanager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import ru.pr1nkos.taskmanager.annotation.Sanitized;

public record CreateSubtaskRequest(

        @NotBlank(message = "Title cannot be empty")
        @Size(max = 100, message = "Title must be less than 100 characters")
        @Sanitized
        String title,

        @Size(max = 500, message = "Description must be less than 500 characters")
        @Sanitized
        String description,

        @NotNull(message = "Assignee ID cannot be null")
        @Positive(message = "Assignee ID must be a positive number")
        Long assignToMemberId,

        @NotNull(message = "Task ID cannot be null")
        @Positive(message = "Task ID must be a positive number")
        Long taskId
) {}
