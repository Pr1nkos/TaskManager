package ru.pr1nkos.taskmanager.dto.request;

import jakarta.annotation.Nullable;

public record UpdateProjectRequest(@Nullable String name,
                                   @Nullable String description) {
}
