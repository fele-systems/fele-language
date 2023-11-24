package com.systems.fele.machine.instr;

import com.systems.fele.machine.AbstractMachineFunction;
import com.systems.fele.machine.AbstractMachineInstruction;
import com.systems.fele.machine.StackFrame;
import com.systems.fele.syntax.function.FunctionSymbol;
import com.systems.fele.syntax.tree.AbstractSyntaxTreeNode;

public class CallInstr extends AbstractMachineInstruction {

	private FunctionSymbol callee;
	private AbstractSyntaxTreeNode source;

	public CallInstr(AbstractSyntaxTreeNode source, FunctionSymbol callee) {
		this.source = source;
		this.callee = callee;

	}
	
	@Override
	public AbstractSyntaxTreeNode getASTNode() {
		return source;
	}

	@Override
	public void execute(AbstractMachineFunction machine, StackFrame frame) {
		callee.getAbstractMachineContext().execute();
	}

	@Override
	public String disassemble() {
		return "CALL " + callee.getName();
	}

}
