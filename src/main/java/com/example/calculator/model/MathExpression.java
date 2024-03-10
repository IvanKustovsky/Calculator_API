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
        return calcMathExpression(mathExpression);
    }

    public boolean isValid() {
        Stack<String> stack = new Stack<>();
        String[] strings = mathExpression.trim().split("(?<=[-+*/%()^])|(?=[-+*/%()^])|(?<=sqrt)|(?=sqrt)");

        for (int i = 0; i < strings.length; i++) {
            String str = strings[i];
            if (str.equals("(")) {
                stack.push(str);
            } else if (str.equals(")")) {
                if (stack.isEmpty() || !stack.pop().equals("(")) {
                    return false;
                }
                // Перевіряємо, чи після закривної дужки іде оператор
                if (i + 1 < strings.length && !isOperator(strings[i + 1])) {
                    return false;
                }
            } else if (isOperator(str)) {
                if (isSquare(str)) {
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
                if (!isNumber(str) && !isTrigonometricFunction(str)) {
                    return false;
                }
            }
        }
        // Перевіряємо, чи стек після всіх операцій порожній (тобто всі дужки закриті)
        return stack.isEmpty();
    }

    private double calcMathExpression(String mathExpression) {
        if (!isValid()) {
            throw new IllegalMathExpressionException();
        }
        double result = 0;
        String[] strings = mathExpression.trim().split("(?<=[-+*/%()^])|(?=[-+*/%()^])|(?<=sqrt)|(?=sqrt)");
        int i = 0;
        while (i < strings.length) {
            if (strings[i].equals("(")) {
                // обробка виразу у дужках
                int openParenthesesCount = 1;
                int j = i + 1;
                while (j < strings.length) {
                    if (strings[j].equals("(")) {
                        openParenthesesCount++;
                    } else if (strings[j].equals(")")) {
                        openParenthesesCount--;
                        if (openParenthesesCount == 0) {
                            result += calcMathExpression(concatenateStrings(strings, i + 1, j));
                            i = j;
                            break;
                        }
                    }
                    j++;
                }
            } else if (strings[i].equals("+") || strings[i].equals("-")) {
                // Додавання або віднімання
                i++;
                if (i < strings.length && isNumber(strings[i])) {
                    double operand = Double.parseDouble(strings[i]);
                    result = strings[i - 1].equals("+") ? result + operand : result - operand;
                } else {
                    // Обробка виразу у дужках
                    int openParenthesesCount = 1;
                    int j = i + 1;
                    while (j < strings.length) {
                        if (strings[j].equals("(")) {
                            openParenthesesCount++;
                        } else if (strings[j].equals(")")) {
                            openParenthesesCount--;
                            if (openParenthesesCount == 0) {
                                result = strings[i - 1].equals("+") ?
                                        result + calcMathExpression(concatenateStrings(strings, i + 1, j)) :
                                        result - calcMathExpression(concatenateStrings(strings, i + 1, j));
                                i = j;
                                break;
                            }
                        }
                        j++;
                    }
                }
            } else if (strings[i].equals("*") || strings[i].equals("/")) {
                // Множення або ділення
                i++;
                double operand = Double.parseDouble(strings[i]);
                result = strings[i - 1].equals("*") ? result * operand : result / operand;
            } else if (strings[i].equals("%")) {
                // Ділення по модулю
                i++;
                double operand = Double.parseDouble(strings[i]);
                result %= operand;
            } else if (isSquare(strings[i]) || isTrigonometricFunction(strings[i])) {
                i++;
                // Після "sqrt" дужка, ми продовжуємо обробку виразу в дужках
                int openParenthesesCount = 1;
                int j = i + 1;
                while (j < strings.length) {
                    if (strings[j].equals("(")) {
                        openParenthesesCount++;
                    } else if (strings[j].equals(")")) {
                        openParenthesesCount--;
                        if (openParenthesesCount == 0) {
                            result = (isSquare(strings[i - 1])) ? Math.sqrt(calcMathExpression(concatenateStrings(strings, i + 1, j))) :
                                    Math.sin(calcMathExpression(concatenateStrings(strings, i + 1, j)));
                            i = j;
                            break;
                        }
                    }
                    j++;
                }
            } else {
                // Просто число
                result = Double.parseDouble(strings[i]);
            }
            i++;
        }
        return result;
    }

    private boolean isOperator(String str) {
        return "+-*/%sqrt^()".contains(str);
    }

    private boolean isSquare(String str) {
        return str.equals("sqrt");
    }

    private boolean isTrigonometricFunction(String str) {
        return "sincos".contains(str);
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
