package com.example.calculator.exception;

public class IllegalMathExpressionException extends RuntimeException{
    public IllegalMathExpressionException() {
        super("Illegal math expression was passed.");
    }
}
