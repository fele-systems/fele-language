package com.systems.fele.machine;

import com.systems.fele.syntax.tree.AbstractSyntaxTreeNode;

public abstract class AbstractMachineInstruction {
	public abstract AbstractSyntaxTreeNode getASTNode();
	public abstract void execute(AbstractMachineFunction machine, StackFrame frame);
	public abstract String disassemble();
}
