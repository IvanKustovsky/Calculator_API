package com.example.calculator;

import com.example.calculator.model.MathExpression;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalcMathExpressionTests {

    private final MathExpression mathExpression = new MathExpression();

    @Test
    public void testSimpleAddition() {
        mathExpression.setMathExpression("2+3");
        assertEquals(5.0, mathExpression.calcMathExpression(), 0.001);
    }

    @Test
    public void testSimpleSubtraction() {
        mathExpression.setMathExpression("(5-3)");
        assertEquals(2.0, mathExpression.calcMathExpression(), 0.001);
    }

    @Test
    public void testSimpleMultiplication() {
        mathExpression.setMathExpression("2*3");
        assertEquals(6.0, mathExpression.calcMathExpression(), 0.001);
    }

    @Test
    public void testSimpleDivision() {
        mathExpression.setMathExpression("6/2");
        assertEquals(3.0, mathExpression.calcMathExpression(), 0.001);
    }

    @Test
    public void testComplexExpressionWithParentheses() {
        mathExpression.setMathExpression("(2+3)*4-(6/2)");
        assertEquals(17.0, mathExpression.calcMathExpression(), 0.001);
    }

    @Test
    public void testSimpleSquareRoot() {
        mathExpression.setMathExpression("sqrt(2+7)");
        assertEquals(3.0, mathExpression.calcMathExpression(), 0.001);
    }

    @Test
    public void testSimpleSquareRoot1() {
        mathExpression.setMathExpression("sqrt(16)");
        assertEquals(4.0, mathExpression.calcMathExpression(), 0.001);
    }

    @Test
    public void testDivideByModule() {
        mathExpression.setMathExpression("8%3");
        assertEquals(2.0, mathExpression.calcMathExpression(), 0.001);
    }
    @Test
    public void testComplexExpressionWithParenthesesAndOperators() {
        mathExpression.setMathExpression("((2+3)*4-(6/2))%3");
        assertEquals(2.0, mathExpression.calcMathExpression(), 0.001);
    }

    @Test
    public void testDivideByModuleWithParentheses() {
        mathExpression.setMathExpression("17%(3/1)");
        assertEquals(2.0, mathExpression.calcMathExpression(), 0.001);
    }

    @Test
    public void testSin() {
        mathExpression.setMathExpression("sin(3.14/2)");
        assertEquals(1.0, mathExpression.calcMathExpression(), 0.001);
    }

}

