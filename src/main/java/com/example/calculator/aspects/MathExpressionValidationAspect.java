package com.example.calculator.aspects;

import com.example.calculator.exception.IllegalMathExpressionException;
import com.example.calculator.validations.MathExpressionValidator;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MathExpressionValidationAspect {

    private final MathExpressionValidator mathExpressionValidator;

    @Autowired
    public MathExpressionValidationAspect(MathExpressionValidator mathExpressionValidator) {
        this.mathExpressionValidator = mathExpressionValidator;
    }

    @Before("@annotation(com.example.calculator.annotation.ExpressionValidator) && args(mathExpression,..)")
    public void validateMathExpression(String mathExpression) {
        if (!mathExpressionValidator.isValid(mathExpression, null)) {
            throw new IllegalMathExpressionException();
        }
    }
}
