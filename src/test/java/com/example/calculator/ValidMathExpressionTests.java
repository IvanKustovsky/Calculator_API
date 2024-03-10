package com.example.calculator;

import com.example.calculator.model.MathExpression;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidMathExpressionTests {

    private final MathExpression mathExpression = new MathExpression();
    @Test
    void testValidExpression() {
        mathExpression.setMathExpression("2+3*5");
        assertTrue(mathExpression.isValid());
    }

    @Test
    void testInvalidExpressionWithInvalidOperator() {
        mathExpression.setMathExpression("2+3#5");
        assertFalse(mathExpression.isValid());
    }

    @Test
    void testInvalidExpressionWithMultipleOperatorsInARow() {
        mathExpression.setMathExpression("2++3");
        assertFalse(mathExpression.isValid());
    }

    @Test
    void testInvalidExpressionWithUnclosedBracket() {
        mathExpression.setMathExpression("(2.76+3.9*5");
        assertFalse(mathExpression.isValid());
    }

    @Test
    void testValidExpressionWithBrackets() {
        mathExpression.setMathExpression("(2+3)*5");
        assertTrue(mathExpression.isValid());
    }

    @Test
    void testValidExpressionWithNegativeSquare() {
        mathExpression.setMathExpression("√-7");
        assertFalse(mathExpression.isValid());
    }

    @Test
    void testValidExpressionWithNegativeDegree() {
        mathExpression.setMathExpression("5^-2");
        assertFalse(mathExpression.isValid());
    }
    @Test
    void testValidExpressionWithNegativeDegreeInBrackets() {
        mathExpression.setMathExpression("5^(-2)");
        assertTrue(mathExpression.isValid());
    }

    @Test
    void testValidExpressionWithSinAndCos() {
        mathExpression.setMathExpression("sin(cos(55.6))");
        assertTrue(mathExpression.isValid());
    }

    @Test
    void testValidExpressionWithEverything() {
        mathExpression.setMathExpression("5*2-(sin(7%2)/cos(9.65*2))^2");
        assertTrue(mathExpression.isValid());
    }

    @Test
    public void testDivisionByZero() {
        mathExpression.setMathExpression("5/0");
        assertFalse(mathExpression.isValid());
    }

    @Test
    public void testModulusByZero() {
        mathExpression.setMathExpression("10%0");
        assertFalse(mathExpression.isValid());
    }

    @Test
    public void testValidExpressionWithDivision() {
        mathExpression.setMathExpression("10/5+(7-3)*2");
        assertTrue(mathExpression.isValid());
    }
}
