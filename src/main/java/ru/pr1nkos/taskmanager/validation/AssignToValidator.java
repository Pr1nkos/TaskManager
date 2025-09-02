package ru.pr1nkos.taskmanager.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.pr1nkos.taskmanager.annotation.ValidAssignTo;

import java.util.Objects;
import java.util.Set;

public class AssignToValidator implements ConstraintValidator<ValidAssignTo, Set<Long>> {

    @Override
    public boolean isValid(Set<Long> value, ConstraintValidatorContext context) {
        // Если поле не передано — разрешаем (опционально)
        if (value == null) {
            return true;
        }

        // Проверяем, что все ID положительные
        return value.stream()
                .allMatch(Objects::nonNull) && // не null
                value.stream().allMatch(id -> id > 0); // положительные
    }
}
