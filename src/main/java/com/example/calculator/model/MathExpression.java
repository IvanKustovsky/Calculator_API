package com.example.calculator.model;

import com.example.calculator.annotation.ExpressionValidator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;


@Setter
@Getter
@Component
public class MathExpression {
    @ExpressionValidator
    private String mathExpression;
}
