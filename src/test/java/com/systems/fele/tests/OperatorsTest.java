package com.systems.fele.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.systems.fele.syntax.Parser;

public class OperatorsTest {

    @ParameterizedTest(name = "Simple math operators: {0} = {1}")
    @CsvSource({
        "256+1,258",
        "100-10,90",
        "25-30,-5",
        "7*8,56",
        "64/8,8"
    })
    void testSimpleMath(String expression, int expectedResult) {
        var parser = new Parser(expression);
        var program = parser.parseExpression();
        program.getMainFunction()
            .execute();
    
        assertEquals(expectedResult, program.getMachine().registerStack.pop().asInt());
    }
    
    @ParameterizedTest(name = "Operator precedence: {0} = {1}")
    @CsvSource({
        "256+1*10,266",
        "(256+1)*10,2570",
        "100-10*10,0",
        "(100-10)*10,900",
        "4*6/8,3"
    })
    void testOperatorPrecedence(String expression, int expectedResult) {
        var parser = new Parser(expression);
        var program = parser.parseExpression();
        program.getMainFunction()
            .execute(); 

        var exit = program.getMachine().registerStack.pop().asInt();
        if (exit != expectedResult) {
            program.dump(System.out);
        }
    
        assertEquals(expectedResult, exit);
    }
}
