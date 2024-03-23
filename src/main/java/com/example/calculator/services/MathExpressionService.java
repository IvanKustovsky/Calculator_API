package com.example.calculator.services;

import com.example.calculator.annotation.ExpressionValidator;
import com.example.calculator.exception.IllegalMathExpressionException;
import com.example.calculator.model.MathExpression;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
@Getter
@Setter
public class MathExpressionService {

    private MathExpression mathExpression = new MathExpression();

    @ExpressionValidator
    public double calculateMathExpression(String expression) throws IllegalMathExpressionException {
        mathExpression.setMathExpression(expression);
        mathExpression.setResult(calcMathExpression(mathExpression.getMathExpression()));
        return mathExpression.getResult();
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
        return switch (operand) {
            case "e" -> Math.E;
            case "π" -> Math.PI;
            default -> Double.parseDouble(operand);
        };
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

    private String concatenateStrings(String[] strings, int startIndex, int endIndex) {
        StringBuilder concatenatedString = new StringBuilder();
        for (int k = startIndex; k < endIndex; k++) {
            concatenatedString.append(strings[k]);
        }
        return concatenatedString.toString();
    }
}
