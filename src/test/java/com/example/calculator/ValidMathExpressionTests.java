package com.example.calculator;

import com.example.calculator.model.MathExpression;
import com.example.calculator.validations.MathExpressionValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidMathExpressionTests {

    private final MathExpression mathExpression = new MathExpression();
    MathExpressionValidator validator = new MathExpressionValidator();

    @Test
    void testNotValidExpression1() {
        mathExpression.setMathExpression(")");
        assertFalse(validator.isValid(mathExpression.getMathExpression(),null));
    }
    @Test
    void testNotValidExpression2() {
        mathExpression.setMathExpression("(");
        assertFalse(validator.isValid(mathExpression.getMathExpression(),null));
    }
    @Test
    void testNotValidExpression3() {
        mathExpression.setMathExpression("* ");
        assertFalse(validator.isValid(mathExpression.getMathExpression(),null));
    }
    @Test
    void testNotValidExpression4() {
        mathExpression.setMathExpression("/");
        assertFalse(validator.isValid(mathExpression.getMathExpression(),null));
    }
    @Test
    void testNotValidExpression5() {
        mathExpression.setMathExpression(" - ");
        assertFalse(validator.isValid(mathExpression.getMathExpression(),null));
    }
    @Test
    void testNotValidExpression6() {
        mathExpression.setMathExpression("+-");
        assertFalse(validator.isValid(mathExpression.getMathExpression(),null));
    }
    @Test
    void testNotValidExpressionWithNegativeSquare() {
        mathExpression.setMathExpression("√-7");
        assertFalse(validator.isValid(mathExpression.getMathExpression(),null));
    }

    @Test
    void testNotValidExpressionWithNegativeDegree() {
        mathExpression.setMathExpression("5^-2");
        assertFalse(validator.isValid(mathExpression.getMathExpression(),null));
    }
    @Test
    void testValidExpression() {
        mathExpression.setMathExpression("2+3*5");
        assertTrue(validator.isValid(mathExpression.getMathExpression(),null));
    }

    @Test
    void testInvalidExpressionWithInvalidOperator() {
        mathExpression.setMathExpression("2+3#5");
        assertFalse(validator.isValid(mathExpression.getMathExpression(),null));
    }

    @Test
    void testInvalidExpressionWithMultipleOperatorsInARow() {
        mathExpression.setMathExpression("2++3");
        assertFalse(validator.isValid(mathExpression.getMathExpression(),null));
    }

    @Test
    void testInvalidExpressionWithUnclosedBracket() {
        mathExpression.setMathExpression("(2.76+3.9*5");
        assertFalse(validator.isValid(mathExpression.getMathExpression(),null));
    }

    @Test
    void testValidExpressionWithBrackets() {
        mathExpression.setMathExpression("(2+3)*5");
        assertTrue(validator.isValid(mathExpression.getMathExpression(),null));
    }


    @Test
    void testValidExpressionWithNegativeDegreeInBrackets() {
        mathExpression.setMathExpression("5^(-2)");
        assertTrue(validator.isValid(mathExpression.getMathExpression(),null));
    }

    @Test
    void testValidExpressionWithSinAndCos() {
        mathExpression.setMathExpression("sin(cos(55.6))");
        assertTrue(validator.isValid(mathExpression.getMathExpression(),null));
    }

    @Test
    void testValidExpressionWithEverything() {
        mathExpression.setMathExpression("5*2-(sin(7%2)/cos(9.65*2))^2");
        assertTrue(validator.isValid(mathExpression.getMathExpression(),null));
    }

    @Test
    public void testDivisionByZero() {
        mathExpression.setMathExpression("5/0");
        assertFalse(validator.isValid(mathExpression.getMathExpression(),null));
    }

    @Test
    public void testModulusByZero() {
        mathExpression.setMathExpression("10%0");
        assertFalse(validator.isValid(mathExpression.getMathExpression(),null));
    }

    @Test
    public void testValidExpressionWithDivision() {
        mathExpression.setMathExpression("10/5+(7-3)*2");
        assertTrue(validator.isValid(mathExpression.getMathExpression(),null));
    }

    @Test
    public void testValidExpressionWithSqrt() {
        mathExpression.setMathExpression("sqrt(16)");
        assertTrue(validator.isValid(mathExpression.getMathExpression(),null));
    }

    @Test
    public void testValidExpressionWithSqrt2() {
        mathExpression.setMathExpression("sqrt(7+2)");
        assertTrue(validator.isValid(mathExpression.getMathExpression(),null));
    }

    @Test
    public void testValidExpressionWithConstant() {
        mathExpression.setMathExpression("π-1");
        assertTrue(validator.isValid(mathExpression.getMathExpression(),null));
    }

    @Test
    public void testValidExpressionWithConstants() {
        mathExpression.setMathExpression("π-e");
        assertTrue(validator.isValid(mathExpression.getMathExpression(),null));
    }
    @Test
    public void testValidExpressionWithConstantsAndBrackets() {
        mathExpression.setMathExpression("(π-e^(2-3))");
        assertTrue(validator.isValid(mathExpression.getMathExpression(),null));
    }

}
