package com.example.calculator.config;

import com.example.calculator.validations.MathExpressionValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public MathExpressionValidator mathExpressionValidator() {
        return new MathExpressionValidator();
    }
}
