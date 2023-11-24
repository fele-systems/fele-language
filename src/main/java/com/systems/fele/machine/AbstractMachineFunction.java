package com.systems.fele.machine;

import java.io.PrintStream;
import java.util.List;
import java.util.stream.Collectors;

import com.systems.fele.machine.instr.RetInstr;
import com.systems.fele.syntax.AbstractSyntaxTree;
import com.systems.fele.syntax.Context;
import com.systems.fele.syntax.SymbolType;
import com.systems.fele.syntax.tree.AbstractSyntaxTreeNode;
import com.systems.fele.syntax.variable.LocalVariableSymbol;

public class AbstractMachineFunction {
	AbstractMachineInstruction[] instr;
	List<LocalVariableSymbol> locals;
	private Context parseContext;
	private AbstractMachine machine;

	public AbstractMachineFunction(AbstractMachine machine, Context parseContext) {
		this.machine = machine;
		this.parseContext = parseContext;
		this.locals = parseContext.getDefinedSymbols()
				.stream()
				.filter(s -> s.getSymbolType() == SymbolType.variable_definition)
				.map(LocalVariableSymbol.class::cast)
				.collect(Collectors.toList());
	}
	
	public AbstractMachineFunction(AbstractMachine machine, AbstractMachineInstruction[] instr, Context parseContext) {
		this.machine = machine;
		this.instr = instr;
		this.parseContext = parseContext;
		this.locals = parseContext.getDefinedSymbols()
				.stream()
				.filter(s -> s.getSymbolType() == SymbolType.variable_definition)
				.map(LocalVariableSymbol.class::cast)
				.collect(Collectors.toList());
	}
	
	public AbstractMachineFunction(AbstractMachine machine, List<AbstractSyntaxTreeNode> statements, Context parseContext) {
		this.machine = machine;
		this.parseContext = parseContext;
		this.locals = parseContext.getDefinedSymbols()
				.stream()
				.filter(s -> s.getSymbolType() == SymbolType.variable_definition)
				.map(LocalVariableSymbol.class::cast)
				.collect(Collectors.toList());
		if (statements != null)
			setStatements(statements);
	}
	
	public void setStatements(List<AbstractSyntaxTreeNode> statements) {
		instr = InstructionBuilder.buildNodes(this, statements.toArray(AbstractSyntaxTreeNode[]::new), parseContext);
	}
	
	public void setInstructions(AbstractMachineInstruction[] instr) {
		this.instr = instr;
	}

	public AbstractMachineFunction(AbstractMachine machine, AbstractSyntaxTree ast, Context parseContext) {
		this.machine = machine;
		this.parseContext = parseContext;
		this.locals = parseContext.getDefinedSymbols()
				.stream()
				.filter(s -> s.getSymbolType() == SymbolType.variable_definition)
				.map(LocalVariableSymbol.class::cast)
				.collect(Collectors.toList());
		this.instr = InstructionBuilder.build(this, ast);
	}

	public void disassemble(PrintStream os) {
		for (var i : instr) {
			os.println(i.disassemble());
		}
	}

	public void execute() {
		var frame = createStackFrame();
		machine.pushStackFrame(frame);
		for (var i : instr) {
			i.execute(this, frame);
			if (i instanceof RetInstr) {
				break;
			}
		}
		machine.popStackFrame();
	}
	
	public void executeDebug(PrintStream os) {
		var frame = createStackFrame();
		machine.pushStackFrame(frame);
		for (var i : instr) {
			os.println(i.disassemble());
			i.execute(this, frame);
		}
		machine.popStackFrame();
	}

	public AbstractMachine getMachine() {
		return machine;
	}
	
	public int getLocalIndex(String local) {
		for (int i = 0; i < locals.size(); i++) {
			if (locals.get(i).getName().equals(local))
				return i;
		}
		
		return -1;
	}
	
	public Context getParseContext() {
		return parseContext;
	}
	
	public StackFrame createStackFrame() {
		var stackFrame = new StackFrame();
		stackFrame.function = this;
		stackFrame.locals = locals.stream()
				.map(s -> new AbstractMachineValue(null, 0, s.getAbstractMachineType()))
				.toArray(AbstractMachineValue[]::new);
		
		return stackFrame;
	}
}
