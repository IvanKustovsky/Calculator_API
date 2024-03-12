package com.example.calculator.validations;

import com.example.calculator.annotation.ExpressionValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Stack;

public class MathExpressionValidator implements ConstraintValidator<ExpressionValidator, String> {

    @Override
    public void initialize(ExpressionValidator constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
    @Override
    public boolean isValid(String mathExpression, ConstraintValidatorContext constraintValidatorContext) {
        Stack<String> stack = new Stack<>();
        String[] strings = mathExpression.trim().split("(?<=[-+*/%()^eπ])|(?=[-+*/%()^eπ])|(?<=sqrt)|(?=sqrt)|(?<=sin)|(?=sin)|(?<=cos)|(?=cos)");

        for (int i = 0; i < strings.length; i++) {
            String str = strings[i];
            if (str.equals("(")) {
                stack.push(str);
            } else if (str.equals(")")) {
                if (stack.isEmpty() || !stack.pop().equals("(")) {
                    return false;
                }
                // Check if an operator follows the closing bracket
                if (i + 1 < strings.length && !isOperator(strings[i + 1]) && !isBracket(strings[i + 1])) {
                    return false;
                }
            } else if (isOperator(str)) {
                if (isSquareRoot(str)) {
                    continue;
                }
                // Check if there's a number or a closing bracket before the operator
                if (i == 0 || (!isNumber(strings[i - 1]) && !isConstant(strings[i - 1]) && !strings[i - 1].equals(")") && !strings[i - 1].equals("("))) {
                    return false;
                }
                // Check for negative exponent
                if (str.equals("^") && i + 1 < strings.length && strings[i + 1].equals("-")) {
                    return false;
                }
                // Check for division by zero or modulo by zero
                if ((str.equals("/") || str.equals("%")) && i + 1 < strings.length && strings[i + 1].equals("0")) {
                    return false;
                }
            } else {
                if (!isNumber(str) && !isTrigonometricFunction(str) && !isSquareRoot(str) && !isConstant(str)) {
                    return false;
                }
            }
        }
        // Check if the stack is empty after all operations (i.e., all brackets are closed)
        return stack.isEmpty();
    }

    private boolean isConstant(String str) {
        return "eπ".contains(str);
    }

    private boolean isOperator(String str) {
        return "+-*/%^".contains(str);
    }

    private boolean isBracket(String str) {
        return "()".contains(str);
    }

    private boolean isSquareRoot(String str) {
        return str.equals("sqrt");
    }

    private boolean isTrigonometricFunction(String str) {
        return "sin_cos".contains(str);
    }

    private boolean isNumber(String str) {
        return str.matches("-?\\d+(\\.\\d+)?"); // Дозволяє цілі та десяткові числа, може мати мінус перед числом
    }

}
