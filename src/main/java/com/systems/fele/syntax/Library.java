package com.systems.fele.syntax;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.systems.fele.machine.AbstractMachineFunction;
import com.systems.fele.machine.AbstractMachineInstruction;
import com.systems.fele.machine.AbstractMachineType;
import com.systems.fele.machine.AbstractMachineValue;
import com.systems.fele.machine.RegistersStack;
import com.systems.fele.machine.StackFrame;
import com.systems.fele.syntax.ParseContext.ContextType;
import com.systems.fele.syntax.function.FunctionParameter;
import com.systems.fele.syntax.tree.AbstractSyntaxTreeNode;

public class Library {

	CompileUnit cu;

	public Library(CompileUnit cu) {
		super();
		this.cu = cu;
	}
	
	public interface ExternFunction {
		AbstractMachineValue execute(RegistersStack s);
	}
	
	public void defineExternalFunction(String name, ExternFunction externFunction, AbstractMachineType... params) {
		var functionContext = new ParseContext(name, cu, ContextType.EXTERN_FUNCTION);
		var machineFunction = new AbstractMachineFunction(cu.getProgram().getMachine(), functionContext);
		
		machineFunction.setInstructions(new AbstractMachineInstruction[] {
			new AbstractMachineInstruction() {
				@Override
				public AbstractSyntaxTreeNode getASTNode() {
					return null;
				}
				
				@Override
				public void execute(AbstractMachineFunction function, StackFrame frame) {
					var newFrame = new StackFrame();
					newFrame.locals = new AbstractMachineValue[0];
					newFrame.function = machineFunction;
					
					function.getMachine().pushStackFrame(frame);
					externFunction.execute(function.getMachine().registerStack);
					function.getMachine().popStackFrame();
				}
				
				@Override
				public String disassemble() {
					return "CALL_e " + name + ";";
				}
			}
		});
		
		cu.defineFunction(name, IntStream.range(1, params.length).mapToObj(i -> new FunctionParameter(i, "arg" + i, params[i])).collect(Collectors.toList())
				, params[0], machineFunction);
	}
}
