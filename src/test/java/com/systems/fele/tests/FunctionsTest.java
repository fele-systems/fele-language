package com.systems.fele.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import com.systems.fele.interop.FlowFunction;
import com.systems.fele.machine.AbstractMachineType;
import com.systems.fele.syntax.Parser;
import com.systems.fele.syntax.Program;
import com.systems.fele.syntax.function.FunctionSymbol;

class FunctionsTest {
    @Test
    void testFunctionDeclaration() {
        var parser = new Parser("I32 f(I32 a0) { ret 0; }");
        var program = new Program();
        parser.parseSource(program);
        
        var f = program.findFunction("f");

        var params = f.getParameters();
        var returnType = f.getReturnType();
    
        assertEquals(1, params.size());
        assertEquals("a0", params.get(0).getName());
        assertEquals(AbstractMachineType.I32, params.get(0).getType());
        assertEquals(AbstractMachineType.I32, returnType);
    }

    @Test
    void testFunctionCall() {
        var parser = new Parser("I32 f() { ret 62326; }");
        var program = new Program();
        parser.parseSource(program);
        
        var f = new FlowFunction(program.findFunction("f"));

        assertEquals(62326, f.invoke().asInt());
    }

    @Test
    void testFunctionCallWithArgs() {
        var parser = new Parser("I32 sum(I32 x, I32 y) { ret x + y; }");
        var program = new Program();
        parser.parseSource(program);
        
        var f = new FlowFunction(program.findFunction("sum"));

        assertEquals(69, f.invoke(60, 9).asInt());
        assertEquals(1000001, f.invoke(1000000, 1).asInt());
        assertEquals(0, f.invoke(10, -10).asInt());
    }
}
