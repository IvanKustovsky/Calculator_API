package com.example.calculator;

import com.example.calculator.model.MathExpression;
import com.example.calculator.services.MathExpressionService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalcMathExpressionTests {

    private final MathExpressionService mathExpressionService = new MathExpressionService();
    private final MathExpression mathExpression = new MathExpression();

    @Test
    public void testSimpleAddition() {
        mathExpression.setMathExpression("2+3");
        assertEquals(5.0, mathExpressionService.calculateMathExpression(mathExpression.getMathExpression()), 0.001);
    }

    @Test
    public void testSimpleSubtraction() {
        mathExpression.setMathExpression("(5-3)");
        assertEquals(2.0, mathExpressionService.calculateMathExpression(mathExpression.getMathExpression()), 0.001);
    }

    @Test
    public void testSimpleMultiplication() {
        mathExpression.setMathExpression("2*3");
        assertEquals(6.0, mathExpressionService.calculateMathExpression(mathExpression.getMathExpression()), 0.001);
    }

    @Test
    public void testSimpleDivision() {
        mathExpression.setMathExpression("6/2");
        assertEquals(3.0, mathExpressionService.calculateMathExpression(mathExpression.getMathExpression()), 0.001);
    }

    @Test
    public void testComplexExpressionWithParentheses() {
        mathExpression.setMathExpression("(2+3)*4-(6/2)");
        assertEquals(17.0, mathExpressionService.calculateMathExpression(mathExpression.getMathExpression()), 0.001);
    }

    @Test
    public void testSimpleSquareRoot() {
        mathExpression.setMathExpression("sqrt(2+7)");
        assertEquals(3.0, mathExpressionService.calculateMathExpression(mathExpression.getMathExpression()), 0.001);
    }

    @Test
    public void testSimpleSquareRoot1() {
        mathExpression.setMathExpression("sqrt(16)");
        assertEquals(4.0, mathExpressionService.calculateMathExpression(mathExpression.getMathExpression()), 0.001);
    }

    @Test
    public void testDivideByModule() {
        mathExpression.setMathExpression("8%3");
        assertEquals(2.0, mathExpressionService.calculateMathExpression(mathExpression.getMathExpression()), 0.001);
    }
    @Test
    public void testComplexExpressionWithParenthesesAndOperators() {
        mathExpression.setMathExpression("((2+3)*4-(6/2))%3");
        assertEquals(2.0, mathExpressionService.calculateMathExpression(mathExpression.getMathExpression()), 0.001);
    }

    @Test
    public void testDivideByModuleWithParentheses() {
        mathExpression.setMathExpression("17%(3/1)");
        assertEquals(2.0, mathExpressionService.calculateMathExpression(mathExpression.getMathExpression()), 0.001);
    }

    @Test
    public void testSin() {
        mathExpression.setMathExpression("sin(3.14/2)");
        assertEquals(Math.sin(3.14/2), mathExpressionService.calculateMathExpression(mathExpression.getMathExpression()), 0.001);
    }

    @Test
    public void testCos() {
        mathExpression.setMathExpression("cos(3.14/2)");
        assertEquals(Math.cos(3.14/2), mathExpressionService.calculateMathExpression(mathExpression.getMathExpression()), 0.001);
    }

    @Test
    public void testSinAndCos() {
        mathExpression.setMathExpression("sin(cos(3.14/2))");
        assertEquals(Math.sin(Math.cos(3.14/2)), mathExpressionService.calculateMathExpression(mathExpression.getMathExpression()), 0.001);
    }

    @Test
    public void testSinPlusCos() {
        mathExpression.setMathExpression("sin(3.14/2)+cos(3.14/2)");
        assertEquals(Math.sin(3.14/2), mathExpressionService.calculateMathExpression(mathExpression.getMathExpression()), 0.001);
    }

    @Test
    public void testSimpleActionOrder() {
        mathExpression.setMathExpression("2+3*5");
        assertEquals(17.0, mathExpressionService.calculateMathExpression(mathExpression.getMathExpression()), 0.001);
    }

    @Test
    public void testSimpleActionOrder2() {
        mathExpression.setMathExpression("2-3+4^2*5");
        assertEquals(79.0, mathExpressionService.calculateMathExpression(mathExpression.getMathExpression()), 0.001);
    }

    @Test
    public void testSimpleActionOrder3() {
        mathExpression.setMathExpression("2+3*4/2-3");
        assertEquals(5.0, mathExpressionService.calculateMathExpression(mathExpression.getMathExpression()), 0.001);
    }

    @Test
    public void testPow() {
        mathExpression.setMathExpression("2^5");
        assertEquals(32.0, mathExpressionService.calculateMathExpression(mathExpression.getMathExpression()), 0.001);
    }

    @Test
    public void testPowWithBrackets() {
        mathExpression.setMathExpression("2^(5-1)");
        assertEquals(16.0, mathExpressionService.calculateMathExpression(mathExpression.getMathExpression()), 0.001);
    }

    @Test
    public void testWithConstant() {
        mathExpression.setMathExpression("π^2");
        assertEquals(9.87, mathExpressionService.calculateMathExpression(mathExpression.getMathExpression()), 0.001);
    }

    @Test
    public void testWithConstants() {
        mathExpression.setMathExpression("π^e");
        assertEquals(22.459, mathExpressionService.calculateMathExpression(mathExpression.getMathExpression()), 0.001);
    }

    @Test
    public void testWithEverything() {
        mathExpression.setMathExpression("2+3*(4-sqrt(16))/cos(0)%3^2");
        assertEquals(2.0, mathExpressionService.calculateMathExpression(mathExpression.getMathExpression()), 0.001);
    }

    @Test
    public void testWithEverything1() {
        mathExpression.setMathExpression("3*(4-sqrt(16))/cos(0)%3^2");
        assertEquals(0.0, mathExpressionService.calculateMathExpression(mathExpression.getMathExpression()), 0.001);
    }

    @Test
    public void testWithEverything2() {
        mathExpression.setMathExpression("2/cos(0)%3^2");
        assertEquals(2.0, mathExpressionService.calculateMathExpression(mathExpression.getMathExpression()), 0.001);
    }

    @Test
    public void testOrder() {
        mathExpression.setMathExpression("2+3*0/1%3^2");
        assertEquals(2.0, mathExpressionService.calculateMathExpression(mathExpression.getMathExpression()), 0.001);
    }

    @Test
    public void testWithRoot() {
        mathExpression.setMathExpression("4-sqrt(4)");
        assertEquals(2.0, mathExpressionService.calculateMathExpression(mathExpression.getMathExpression()), 0.001);
    }

}

