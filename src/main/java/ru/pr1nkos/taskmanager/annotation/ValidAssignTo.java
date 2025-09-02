package ru.pr1nkos.taskmanager.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.pr1nkos.taskmanager.validation.AssignToValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AssignToValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAssignTo {
    String message() default "Assignee IDs must be valid positive integers";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
