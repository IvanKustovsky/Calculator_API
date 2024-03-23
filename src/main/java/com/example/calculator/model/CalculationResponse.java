package com.example.calculator.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class CalculationResponse extends Response {
    private double result;
    public CalculationResponse(double result, String statusCode, String statusMessage) {
        super(statusCode,statusMessage);
        this.result = result;
    }
    public CalculationResponse(String statusCode, String statusMessage) {
        super(statusCode,statusMessage);
    }
}
