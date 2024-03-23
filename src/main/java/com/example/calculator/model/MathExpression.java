package com.example.calculator.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
public class MathExpression {
    private String mathExpression;
    private double result;
}
