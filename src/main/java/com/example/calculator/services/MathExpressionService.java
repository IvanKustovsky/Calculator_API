package com.example.calculator.services;

import com.example.calculator.model.MathExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MathExpressionService {

    private final MathExpression mathExpression;

    public double calcMathExpression(){
        return 1; //todo
    }
}
