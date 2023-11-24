package com.systems.fele.machine.instr;

import static com.systems.fele.machine.InstructionType._F32;
import static com.systems.fele.machine.InstructionType._I32;

import java.util.Map;
import java.util.function.UnaryOperator;

import com.systems.fele.machine.AbstractMachineFunction;
import com.systems.fele.machine.AbstractMachineInstruction;
import com.systems.fele.machine.AbstractMachineValue;
import com.systems.fele.machine.InstructionType;
import com.systems.fele.machine.StackFrame;
import com.systems.fele.syntax.tree.AbstractSyntaxTreeNode;

public class CastInstr extends AbstractMachineInstruction {

	private AbstractSyntaxTreeNode node;
	private InstructionType from;
	private InstructionType to;

	private Map<InstructionType, Map<InstructionType, UnaryOperator<AbstractMachineValue>>> mapper = Map.ofEntries(
			Map.entry(_I32, 
					Map.ofEntries(
							Map.entry(_F32, CastInstr::from_I32_to_F32)
							)
					),
			Map.entry(_F32, 
					Map.ofEntries(
							Map.entry(_I32, CastInstr::from_F32_to_I32)
							)
					)			
			);
		
	public CastInstr(AbstractSyntaxTreeNode node, InstructionType from, InstructionType to) {
		this.node = node;
		this.from = from;
		this.to = to;
	}
	
	public static AbstractMachineValue from_I32_to_F32(AbstractMachineValue i32Value) {
		return AbstractMachineValue.newF32((float) i32Value.asInt());
	}
	
	public static AbstractMachineValue from_F32_to_I32(AbstractMachineValue f32Value) {
		return AbstractMachineValue.newI32((int) f32Value.asFloat());
	}
	
	@Override
	public AbstractSyntaxTreeNode getASTNode() {
		return node;
	}

	@Override
	public void execute(AbstractMachineFunction context, StackFrame frame) {
		if (from == to) return;

		var v0 = context.getMachine().registerStack.pop();
		var v1 = mapper.get(from).get(to).apply(v0);
		context.getMachine().registerStack.push(v1);
	}

	@Override
	public String disassemble() {
		return "CAST%s%s;".formatted(from, to);
	}
	
}
