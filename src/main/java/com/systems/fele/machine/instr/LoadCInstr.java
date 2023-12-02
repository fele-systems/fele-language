package com.systems.fele.machine.instr;

import com.systems.fele.machine.AbstractMachineFunction;
import com.systems.fele.machine.AbstractMachineInstruction;
import com.systems.fele.machine.InstructionType;
import com.systems.fele.machine.StackFrame;
import com.systems.fele.syntax.tree.AbstractSyntaxTreeNode;
import com.systems.fele.syntax.tree.NumberLiteralNode;

public class LoadCInstr extends AbstractMachineInstruction {

	private final AbstractSyntaxTreeNode literalNode;
	private final InstructionType ldType;

	public LoadCInstr(InstructionType ldType, AbstractSyntaxTreeNode literalNode) {
		this.ldType = ldType;
		this.literalNode = literalNode;
	}
	
	@Override
	public AbstractSyntaxTreeNode getASTNode() {
		return literalNode;
	}

	@Override
	public void execute(AbstractMachineFunction context, StackFrame frame) {
		var val = ((NumberLiteralNode) literalNode).getValue();
		
		assert(val.getType().instrType() == ldType);
		
		context.getMachine().registerStack.push(val);
	}

	@Override
	public String disassemble() {
		return "LD%s %s;".formatted(ldType, literalNode.getSourceToken().text());
	}

}
