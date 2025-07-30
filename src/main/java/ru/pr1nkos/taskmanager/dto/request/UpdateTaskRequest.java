package ru.pr1nkos.taskmanager.dto.request;

import ru.pr1nkos.taskmanager.constant.TaskStatus;

public record UpdateTaskRequest(
        String title,
        String description,
        TaskStatus status
) {}
