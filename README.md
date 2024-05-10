# Calculator API

This project is a calculator API implemented in Java using Spring Boot. It allows you to perform various arithmetic operations using HTTP requests.

## Installation

To use this API, you need to have Java and Maven installed on your computer.

## Launch

1. Copy this repository on your computer.
2. Run the CalculatorApplication class.

## Example of usage:
GET http://localhost:9000/api/calculateExpression?expression=(2-3)*4-(6/2)

Json result:
{
statusCode: "200",
statusMessage: "Calculation was successful.",
result: -7
}
