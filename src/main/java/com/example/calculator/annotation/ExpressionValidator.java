package com.example.calculator.annotation;

import com.example.calculator.validations.MathExpressionValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MathExpressionValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExpressionValidator {
    String message() default "Invalid math expression.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
