package com.example.calculator.controllers;


import com.example.calculator.services.MathExpressionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000/")
@RequiredArgsConstructor
@RestController
public class CalculatorController {
    private final MathExpressionService mathExpressionService;
    @GetMapping("/calculateExpression")
    public double calculateExpression(@RequestParam("expression") String expression) {
        log.warn(expression);
       return mathExpressionService.calculateMathExpression(expression);
    }
}
