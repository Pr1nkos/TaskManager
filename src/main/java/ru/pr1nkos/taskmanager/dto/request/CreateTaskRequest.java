package ru.pr1nkos.taskmanager.dto.request;

public record CreateTaskRequest(
        String title,
        String description,
        Long projectId
) {}
