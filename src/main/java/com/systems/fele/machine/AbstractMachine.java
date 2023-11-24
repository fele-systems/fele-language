package com.systems.fele.machine;

import java.io.PrintStream;
import java.util.Stack;

public class AbstractMachine {
	
	//public final AbstractSyntaxTree ast;
	public final RegistersStack registerStack = new RegistersStack();
	//public final AbstractMachineContext globalContext;
	public int basePointer = 256;
	public final StackFrame[] stack = new StackFrame[256];

	public AbstractMachine() {
		
	}
	
	public void pushStackFrame(StackFrame frame) {
		stack[--basePointer] = frame;
	}
	
	public void popStackFrame() {
		basePointer++;
	}
	
	public void printStackFrame(PrintStream e) {
		for (int i = basePointer; i < stack.length; i++) {
			e.print("%s@%d".formatted(stack[i].function.getParseContext().getContextName()));
		}
	}
	
	/*public AbstractMachine(AbstractSyntaxTree ast) {
		this.ast = ast;
		this.globalContext = new AbstractMachineContext(InstructionBuilder.build(ast), ast.globalContext);
		
	}*/
	
	
}
