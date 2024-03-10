package com.example.calculator.model;

import com.example.calculator.exception.IllegalMathExpressionException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Stack;

@NoArgsConstructor
@Setter
@Getter
@Component
public class MathExpression {

    private String mathExpression;

    public double calcMathExpression() {
        if (!isValid()) {
            throw new IllegalMathExpressionException();
        }
        return calcMathExpression(mathExpression);
    }

    public boolean isValid() {
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

    private double calcMathExpression(String mathExpression) {
        double result = 0;
        String[] strings = mathExpression.trim().
                split("(?<=[-+*/%()^eπ])|(?=[-+*/%()^eπ])|(?<=sqrt)|(?=sqrt)|(?<=sin)|(?=sin)|(?<=cos)|(?=cos)");
        int index = 0;
        while (index < strings.length) {
            if (strings[index].equals("(")) {
                // обробка виразу у дужках
                int openParenthesesCount = 1;
                int j = index + 1;
                while (j < strings.length) {
                    if (strings[j].equals("(")) {
                        openParenthesesCount++;
                    } else if (strings[j].equals(")")) {
                        openParenthesesCount--;
                        if (openParenthesesCount == 0) {
                            result += calcMathExpression(concatenateStrings(strings, index + 1, j));
                            index = j;
                            break;
                        }
                    }
                    j++;
                }
            } else if (isOperator(strings[index]) || isSquareRoot(strings[index]) || isTrigonometricFunction(strings[index])) {
                // Оператори +,-,*,/,%,^
                index++;
                if (index < strings.length && (isNumber(strings[index]) || isConstant(strings[index]))) {
                    double operand = getOperandValue(strings[index]);
                    switch (strings[index - 1]) {
                        case "+":
                            if (index + 1 < strings.length && strings[index + 1] != null && "*/%^".contains(strings[index + 1])) {
                                result += calcMathExpression(concatenateStrings(strings, index, strings.length));
                                return result;
                            }
                            result += operand;
                            break;
                        case "-":
                            if (index + 1 < strings.length && strings[index + 1] != null && "*/%^".contains(strings[index + 1])) {
                                result -= calcMathExpression(concatenateStrings(strings, index, strings.length));
                                return result;
                            }
                            result -= operand;
                            break;
                        case "*":
                            result *= operand;
                            break;
                        case "/":
                            result /= operand;
                            break;
                        case "%":
                            result %= operand;
                            break;
                        case "^":
                            result = Math.pow(result, operand);
                            break;
                    }
                } else {
                    // Обробка виразу у дужках
                    int openParenthesesCount = 1;
                    int j = index + 1;
                    while (j < strings.length) {
                        if (strings[j].equals("(")) {
                            openParenthesesCount++;
                        } else if (strings[j].equals(")")) {
                            openParenthesesCount--;
                            if (openParenthesesCount == 0) {
                                switch (strings[index - 1]) {
                                    case "+":
                                        result += calcMathExpression(concatenateStrings(strings, index + 1, j));
                                        break;
                                    case "-":
                                        result -= calcMathExpression(concatenateStrings(strings, index + 1, j));
                                        break;
                                    case "*":
                                        result *= calcMathExpression(concatenateStrings(strings, index + 1, j));
                                        break;
                                    case "/":
                                        result /= calcMathExpression(concatenateStrings(strings, index + 1, j));
                                        break;
                                    case "%":
                                        result %= calcMathExpression(concatenateStrings(strings, index + 1, j));
                                        break;
                                    case "^":
                                        result = Math.pow(result, calcMathExpression(concatenateStrings(strings, index + 1, j)));
                                        break;
                                    case "sin":
                                        result = Math.sin(calcMathExpression(concatenateStrings(strings, index + 1, j)));
                                        break;
                                    case "cos":
                                        result = Math.cos(calcMathExpression(concatenateStrings(strings, index + 1, j)));
                                        break;
                                    case "sqrt":
                                        result = Math.sqrt(calcMathExpression(concatenateStrings(strings, index + 1, j)));
                                        break;
                                }
                                index = j;
                                break;
                            }
                        }
                        j++;
                    }
                }
            } else {
                // Просто число чи константа
                result = getOperandValue(strings[index]);
            }
            index++;
        }
        return result;
    }

    private double getOperandValue(String operand) {
        switch (operand) {
            case "e":
                return Math.E;
            case "π":
                return Math.PI;
            default:
                return Double.parseDouble(operand);
        }
    }

        private boolean isOperator(String str){
            return "+-*/%^".contains(str);
        }

        private boolean isBracket(String str){
            return "()".contains(str);
        }

        private boolean isSquareRoot(String str){
            return str.equals("sqrt");
        }

        private boolean isTrigonometricFunction(String str){
            return "sin_cos".contains(str);
        }

        private boolean isNumber(String str){
            return str.matches("-?\\d+(\\.\\d+)?"); // Дозволяє цілі та десяткові числа, може мати мінус перед числом
        }

        private String concatenateStrings(String[]strings, int startIndex, int endIndex) {
            StringBuilder concatenatedString = new StringBuilder();
            for (int k = startIndex; k < endIndex; k++) {
                concatenatedString.append(strings[k]);
            }
            return concatenatedString.toString();
        }
    }
