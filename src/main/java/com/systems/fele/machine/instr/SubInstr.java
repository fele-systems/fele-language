package com.systems.fele.machine.instr;

import com.systems.fele.machine.AbstractMachineFunction;
import com.systems.fele.machine.AbstractMachineInstruction;
import com.systems.fele.machine.AbstractMachineValue;
import com.systems.fele.machine.InstructionType;
import com.systems.fele.machine.StackFrame;
import com.systems.fele.syntax.tree.AbstractSyntaxTreeNode;
import com.systems.fele.syntax.tree.BinaryOperatorNode;

public class SubInstr extends AbstractMachineInstruction {

	InstructionType type;
	BinaryOperatorNode binaryOperator;
	
	public SubInstr(BinaryOperatorNode binaryOperator, InstructionType type) {
		this.binaryOperator = binaryOperator;
		this.type = type;
	}

	@Override
	public AbstractSyntaxTreeNode getASTNode() {
		return binaryOperator;
	}

	@Override
	public void execute(AbstractMachineFunction context, StackFrame frame) {
		var machine = context.getMachine();
		var v0 = machine.registerStack.pop();
		var v1 = machine.registerStack.pop();
		
		assert(v0.getType() == v1.getType());
		
		var v2 = switch(type) {
		case _I8  -> AbstractMachineValue.newI32(v0.asByte() - v1.asByte());
		case _I32 -> AbstractMachineValue.newI32(v0.asInt() - v1.asInt());
		case _F32 -> AbstractMachineValue.newF32(v0.asFloat() - v1.asFloat());
		case _Ref -> throw new RuntimeException("Unsupported instruction type " + type + " for: Sub");
		};
		
		machine.registerStack.push(v2);
	}

	@Override
	public String disassemble() {
		return "SUB%s".formatted(type);
	}
}