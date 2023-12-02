package com.systems.fele.machine.instr;

import com.systems.fele.machine.AbstractMachineFunction;
import com.systems.fele.machine.AbstractMachineInstruction;
import com.systems.fele.machine.InstructionType;
import com.systems.fele.machine.StackFrame;
import com.systems.fele.syntax.tree.AbstractSyntaxTreeNode;

public class LoadAInstr extends AbstractMachineInstruction {
    private final AbstractSyntaxTreeNode literalNode;
	private final InstructionType ldType;
	private int localIndex;

    public LoadAInstr(InstructionType ldType, AbstractSyntaxTreeNode literalNode, int localIndex) {
		this.ldType = ldType;
		this.literalNode = literalNode;
		this.localIndex = localIndex;
	}
	
	@Override
	public AbstractSyntaxTreeNode getASTNode() {
		return literalNode;
	}

	@Override
	public void execute(AbstractMachineFunction context, StackFrame frame) {
		var local = frame.arguments[localIndex];
		if (local.getType().instrType() != ldType)
			throw new RuntimeException("Bad frame. Expected " + ldType + ", but loaded local was: " + local.getType().instrType());
		
		
		context.getMachine().registerStack.push(local);
	}

	@Override
	public String disassemble() {
		return "LD%s %s;".formatted(ldType, literalNode.getSourceToken().text());
	}
}
