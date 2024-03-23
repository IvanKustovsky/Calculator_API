package com.example.calculator.controllers;

import com.example.calculator.exception.IllegalMathExpressionException;
import com.example.calculator.model.CalculationResponse;
import com.example.calculator.services.MathExpressionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000/")
@RequiredArgsConstructor
@RestController
public class CalculatorController {
    private final MathExpressionService mathExpressionService;

    @GetMapping("/calculateExpression")
    public ResponseEntity<CalculationResponse> calculateExpression(@RequestParam("expression") String expression) {
        CalculationResponse calculationResponse;
        try {
            double result = mathExpressionService.calculateMathExpression(expression);
            calculationResponse = new CalculationResponse
                    (result, "200", "Calculation was successful.");
        } catch (IllegalMathExpressionException e) {
            log.error("Illegal math expression: {}", expression, e);
            calculationResponse = new CalculationResponse("400", "Bad request.");
        } catch (Exception e) {
            log.error("Error calculating expression: {}", expression, e);
            calculationResponse = new CalculationResponse("500", "Server Error.");
        }
        return ResponseEntity.ok(calculationResponse);
    }
}
