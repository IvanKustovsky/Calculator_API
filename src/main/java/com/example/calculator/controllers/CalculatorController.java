package com.example.calculator.controllers;


import com.example.calculator.model.MathExpression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@Slf4j
public class CalculatorController {

    private final MathExpression mathExpression = new MathExpression();
    @GetMapping("/calculateExpression")
    public double calculateExpression(@RequestParam("expression") String expression) {
        mathExpression.setMathExpression(expression);
        log.warn(expression);
       return mathExpression.calcMathExpression();
    }
    @GetMapping("/calculate")
    public double calculate(@RequestParam("num1") double num1,
                            @RequestParam(name = "action") String action,
                            @RequestParam("num2") double num2) {
        if(action.equals("plus")) {
            return num1 + num2;
        }
        if(action.equals("subtract")) {
            return num1 - num2;
        }
        if(action.equals("multiply")) {
            return num1 * num2;
        }
        if(action.equals("divide")) {
            return num1 / num2;
        }
        return 0;
    }

    @GetMapping("/add")
    public double add(@RequestParam("num1") double num1, @RequestParam("num2") double num2) {
        return num1 + num2;
    }

    @GetMapping("/subtract")
    public double subtract(@RequestParam("num1") double num1, @RequestParam("num2") double num2) {
        return num1 - num2;
    }

    @GetMapping("/multiply")
    public double multiply(@RequestParam("num1") double num1, @RequestParam("num2") double num2) {
        return num1 * num2;
    }

    @GetMapping("/divide")
    public double divide(@RequestParam("num1") double num1, @RequestParam("num2") double num2) {
        return num1 / num2;
    }

}
