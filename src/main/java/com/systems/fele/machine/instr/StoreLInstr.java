package com.systems.fele.machine.instr;

import com.systems.fele.machine.AbstractMachineFunction;
import com.systems.fele.machine.AbstractMachineInstruction;
import com.systems.fele.machine.InstructionType;
import com.systems.fele.machine.StackFrame;
import com.systems.fele.syntax.tree.AbstractSyntaxTreeNode;
import com.systems.fele.syntax.tree.AssignmentNode;

public class StoreLInstr extends AbstractMachineInstruction {

	private AssignmentNode assignNode;
	private InstructionType stType;
	private int localIndex;

	public StoreLInstr(AssignmentNode assignNode, InstructionType stType, int localIndex) {
		this.assignNode = assignNode;
		this.stType = stType;
		this.localIndex = localIndex;
		
	}	
	
	@Override
	public AbstractSyntaxTreeNode getASTNode() {
		return assignNode;
	}

	@Override
	public void execute(AbstractMachineFunction context, StackFrame frame) {
		var value = context.getMachine().registerStack.pop();
		if (value.getType().instrType() != stType)
			throw new RuntimeException("Bad stack: Expected " + stType + " but top of stack was: " + value.getType().instrType());
		
		frame.locals[localIndex] = value;
	}

	@Override
	public String disassemble() {
		return "ST%s %s;".formatted(stType, assignNode.getLhs().getSourceToken().text());
	}

}
