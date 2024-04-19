package com.example.calculator.services;

import com.example.calculator.annotation.ExpressionValidator;
import com.example.calculator.constants.CalculationSymbols;
import com.example.calculator.model.MathExpression;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class MathExpressionService {

    private MathExpression mathExpression = new MathExpression();

    @ExpressionValidator
    public double calculateMathExpression(String expression) {
        mathExpression.setMathExpression(expression);
        mathExpression.setResult(calcMathExpression(mathExpression.getMathExpression()));
        return mathExpression.getResult();
    }

    private double calcMathExpression(String expression) {
        String[] strings = expression.trim().
                split("(?<=[-+*/%()^eπ])|(?=[-+*/%()^eπ])|(?<=sqrt)|(?=sqrt)|(?<=sin)|(?=sin)|(?<=cos)|(?=cos)");
        int index = 0;
        while (index < strings.length) {
            if (strings[index].equals("(")) {
                index = processExpressionInParentheses(strings, index); // обробка виразу у дужках
            } else if (isOperator(strings[index]) || isFunction(strings[index])) {
                // Оператори + , - , * , / , % , ^ , sin() , cos() , sqrt()
                index = (index == 0) ? index : index + 1;
                if (index < strings.length && (isNumber(strings[index]) || isConstant(strings[index]) || isFunction(strings[index]))) {
                    double operand;
                    if(isFunction(strings[index])) {
                        double currentRes = mathExpression.getResult();
                        operand = calcTrigonometricFunctionOrSquareRoot(strings, index + 1);
                        mathExpression.setResult(currentRes);
                        int indexOfClosingBracket = findIndexOfClosingBracket(strings, index + 1);
                        if(indexOfClosingBracket + 1 >= strings.length) {
                            if(index == 0){
                                return operand;
                            }
                        } else {
                            strings[indexOfClosingBracket] = String.valueOf(operand);
                            return calculateMathExpression(concatenateStrings(strings, indexOfClosingBracket, strings.length));
                        }
                    } else {
                        operand = getOperandValue(strings[index]);
                    }
                    switch (strings[index - 1]) {
                        case "+" -> {
                            if (index + 1 < strings.length && isHigherPriorityOperation(strings[index + 1])) {
                                mathExpression.setResult(mathExpression.getResult() +
                                        calcMathExpression(concatenateStrings(strings, index, strings.length)));
                                return mathExpression.getResult();
                            }
                            mathExpression.setResult(mathExpression.getResult() + operand);
                        }
                        case "-" -> {
                            if (index + 1 < strings.length && isHigherPriorityOperation(strings[index + 1])) {
                                mathExpression.setResult(mathExpression.getResult() -
                                        calcMathExpression(concatenateStrings(strings, index, strings.length)));
                                return mathExpression.getResult();
                            }
                            mathExpression.setResult(mathExpression.getResult() - operand);
                        }
                        case "*" -> mathExpression.setResult(mathExpression.getResult() * operand);
                        case "/" -> mathExpression.setResult(mathExpression.getResult() / operand);
                        case "%" -> mathExpression.setResult(mathExpression.getResult() % operand);
                        case "^" -> mathExpression.setResult(Math.pow(mathExpression.getResult(), operand));
                    }
                } else { // Обробка виразу у дужках
                       index = processExpressionInParentheses(strings, index);
                }
            } else { // Просто число або константа
                mathExpression.setResult(getOperandValue(strings[index]));
            }
            index++;
        }
        return mathExpression.getResult();
    }

    private int processExpressionInParentheses(String[] strings, int index) {
        int indexOfClosingBracket = findIndexOfClosingBracket(strings, index);
        if (indexOfClosingBracket != -1) {
            if (index - 1 < 0 || isFunction(strings[index])) {
                mathExpression.setResult(mathExpression.getResult() +
                        calcMathExpression(concatenateStrings(strings, index + 1, indexOfClosingBracket)));
            } else {
                switch (strings[index - 1]) {
                    case "+" -> mathExpression.setResult(mathExpression.getResult() +
                            calcMathExpression(concatenateStrings(strings, index + 1, indexOfClosingBracket)));
                    case "-" -> mathExpression.setResult(mathExpression.getResult() -
                            calcMathExpression(concatenateStrings(strings, index + 1, indexOfClosingBracket)));
                    case "*" -> mathExpression.setResult(mathExpression.getResult() *
                            calcMathExpression(concatenateStrings(strings, index + 1, indexOfClosingBracket)));
                    case "/" -> mathExpression.setResult(mathExpression.getResult() /
                            calcMathExpression(concatenateStrings(strings, index + 1, indexOfClosingBracket)));
                    case "%" -> mathExpression.setResult(mathExpression.getResult() %
                            calcMathExpression(concatenateStrings(strings, index + 1, indexOfClosingBracket)));
                    case "^" -> mathExpression.setResult(Math.pow(mathExpression.getResult(),
                            calcMathExpression(concatenateStrings(strings, index + 1, indexOfClosingBracket))));
                }
            }
        }
        return indexOfClosingBracket;
    }

    private int findIndexOfClosingBracket(String[] strings, int indexOfOpeningBracket) {
        int indexOfClosingBracket = indexOfOpeningBracket + 1;
        int openParenthesesCount = 1;
        while (indexOfClosingBracket < strings.length) {
            if (strings[indexOfClosingBracket].equals("(")) {
                openParenthesesCount++;
            } else if (strings[indexOfClosingBracket].equals(")")) {
                openParenthesesCount--;
            }
            if (openParenthesesCount == 0) {
                return indexOfClosingBracket;
            }
            indexOfClosingBracket++;
        }
        return -1; // There is no closing bracket
    }

    private double calcTrigonometricFunctionOrSquareRoot(String[] strings, int index) {
        int indexOfClosingBracket = findIndexOfClosingBracket(strings, index);
        if (indexOfClosingBracket != -1) {
            switch (strings[index - 1]) {
                case "sin" -> {
                    return Math.sin(calcMathExpression(concatenateStrings(strings, index + 1, indexOfClosingBracket)));
                }
                case "cos" -> {
                    return Math.cos(calcMathExpression(concatenateStrings(strings, index + 1, indexOfClosingBracket)));
                }
                case "sqrt" -> {
                    return Math.sqrt(calcMathExpression(concatenateStrings(strings, index + 1, indexOfClosingBracket)));
                }
            }
        }
        return 0;
    }

    private double getOperandValue(String operand) {
        return switch (operand) {
            case "e" -> Math.E;
            case "π" -> Math.PI;
            default -> Double.parseDouble(operand);
        };
    }

    private boolean isHigherPriorityOperation(String operation) {
        return operation != null && "*/%^".contains(operation);
    }

    private boolean isOperator(String token) {
        return CalculationSymbols.OPERATORS.contains(token);
    }

    private boolean isFunction(String token) {
        return CalculationSymbols.FUNCTIONS.contains(token);
    }

    private boolean isConstant(String token) {
        return CalculationSymbols.CONSTANTS.contains(token);
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
