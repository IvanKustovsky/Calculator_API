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
        if(!isValid()){
            throw new IllegalMathExpressionException();
        }
        return calcMathExpression(mathExpression);
    }

    public boolean isValid() {
        Stack<String> stack = new Stack<>();
        String[] strings = mathExpression.trim().
                split("(?<=[-+*/%()^])|(?=[-+*/%()^])|(?<=sqrt)|(?=sqrt)|(?<=sin)|(?=sin)|(?<=cos)|(?=cos)");

        for (int i = 0; i < strings.length; i++) {
            String str = strings[i];
            if (str.equals("(")) {
                stack.push(str);
            } else if (str.equals(")")) {
                if (stack.isEmpty() || !stack.pop().equals("(")) {
                    return false;
                }
                // Перевіряємо, чи після закривної дужки іде оператор
                if (i + 1 < strings.length && !isOperator(strings[i + 1]) && !isBracket(strings[i + 1])) {
                    return false;
                }
            } else if (isOperator(str)) {
                if (isSquareRoot(str)) {
                    continue;
                }
                // Перевіряємо, чи перед оператором є число або закривна дужка
                if (i == 0 || (!isNumber(strings[i - 1]) && !strings[i - 1].equals(")") && !strings[i - 1].equals("("))) {
                    return false;
                }
                // Перевіряємо, чи степінь має від'ємний показник
                if (str.equals("^") && i + 1 < strings.length && strings[i + 1].equals("-")) {
                    return false;
                }
                // Перевіряємо ділення на нуль або ділення по модулю на нуль
                if ((str.equals("/") || str.equals("%")) && i + 1 < strings.length && strings[i + 1].equals("0")) {
                    return false;
                }
            } else {
                if (!isNumber(str) && !isTrigonometricFunction(str) && !isSquareRoot(str)) {
                    return false;
                }
            }
        }
        // Перевіряємо, чи стек після всіх операцій порожній (тобто всі дужки закриті)
        return stack.isEmpty();
    }

    private double calcMathExpression(String mathExpression) {
        double result = 0;
        String[] strings = mathExpression.trim().
                split("(?<=[-+*/%()^])|(?=[-+*/%()^])|(?<=sqrt)|(?=sqrt)|(?<=sin)|(?=sin)|(?<=cos)|(?=cos)");
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
                            result += calcMathExpression(concatenateStrings(strings,index + 1, j));
                            index = j;
                            break;
                        }
                    }
                    j++;
                }
            } else if (isOperator(strings[index]) || isSquareRoot(strings[index]) || isTrigonometricFunction(strings[index])) {
                // Оператори +,-,*,/,%,^
                index++;
                if (index < strings.length && isNumber(strings[index])) {
                    double operand = Double.parseDouble(strings[index]);
                    switch (strings[index - 1]) {
                        case "+":
                            if(index + 1 < strings.length && strings[index + 1] != null && "*/%^".contains(strings[index + 1])) {
                                result += calcMathExpression(concatenateStrings(strings, index, strings.length));
                                return result;
                            }
                            result += operand;
                            break;
                        case "-":
                            if(index + 1 < strings.length && strings[index + 1] != null && "*/%^".contains(strings[index + 1])) {
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
                                            result += calcMathExpression(concatenateStrings(strings, index + 1, j)) ;
                                            break;
                                        case "-":
                                            result -= calcMathExpression(concatenateStrings(strings, index + 1, j)) ;
                                            break;
                                        case "*":
                                            result *= calcMathExpression(concatenateStrings(strings, index + 1, j)) ;
                                            break;
                                        case "/":
                                            result /= calcMathExpression(concatenateStrings(strings, index + 1, j)) ;
                                            break;
                                        case "%":
                                            result %= calcMathExpression(concatenateStrings(strings, index + 1, j)) ;
                                            break;
                                        case "^":
                                            result = Math.pow(result, calcMathExpression(concatenateStrings(strings, index + 1, j)) );
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
                    // Просто число
                    result = Double.parseDouble(strings[index]);
                }
                index++;
            }
            return result;
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
