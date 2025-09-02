package ru.pr1nkos.taskmanager.constant;

public final class ExceptionMessages {
    private ExceptionMessages() {
        throw new UnsupportedOperationException("Utility class");
    }

    // Для Task
    public static final String TASK_NOT_FOUND = "Task not found with ID: ";
    public static final String PROJECT_NOT_FOUND = "Project not found with ID: ";
    public static final String MEMBER_NOT_FOUND = "Member not found with ID: ";

    // Можно и с шаблонами (если используешь MessageFormat или String.format)
    public static final String TASK_NOT_FOUND_FMT = "Task not found with ID: {0}";
    public static final String PROJECT_NOT_FOUND_FMT = "Project not found with ID: {0}";
}
