package ru.pr1nkos.taskmanager.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;

public record CreateNewProjectRequest(@NotEmpty String name,
                                      @Nullable String description) {
}
