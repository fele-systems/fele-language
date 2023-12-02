package com.systems.fele.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.systems.fele.machine.AbstractMachineType;
import com.systems.fele.syntax.Parser;
import com.systems.fele.syntax.Program;

public class StructureTest {
    @Test
    public void testFunctionDeclaration() {
        var parser = new Parser("I32 f(I32 a0) { ret 0; }");
        var program = new Program();
        parser.parseSource(program);
        
        var f = program.findFunction("f");

        var params = f.getParameters();
        var returnType = f.getReturnType();
    
        assertEquals(1, params.size());
        assertEquals(params.get(0).getName(), "a0");
        assertEquals(params.get(0).getType(), AbstractMachineType.I32);
        assertEquals(AbstractMachineType.I32, returnType);
    }
}
