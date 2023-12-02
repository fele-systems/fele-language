package com.systems.fele.machine;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.systems.fele.machine.instr.RetInstr;
import com.systems.fele.syntax.AbstractSyntaxTree;
import com.systems.fele.syntax.Context;
import com.systems.fele.syntax.Symbol;
import com.systems.fele.syntax.SymbolType;
import com.systems.fele.syntax.function.FunctionParameter;
import com.systems.fele.syntax.tree.AbstractSyntaxTreeNode;
import com.systems.fele.syntax.variable.LocalVariableSymbol;

public class AbstractMachineFunction {
	AbstractMachineInstruction[] instr;
	private final List<LocalVariableSymbol> locals = new ArrayList<>();
	private final List<FunctionParameter> parameters = new ArrayList<>();
	private final Context parseContext;
	private final AbstractMachine machine;

	public AbstractMachineFunction(AbstractMachine machine, Context parseContext) {
		this.machine = machine;
		this.parseContext = parseContext;
		parseContext.getDefinedSymbols()
				.stream()
				.filter(s -> s.getSymbolType() == SymbolType.variable_definition)
				.forEach(this::addSymbol);
	}

	private void addSymbol(Symbol symbol) {
		if (symbol instanceof LocalVariableSymbol local) {
			locals.add(local);
		} else if (symbol instanceof FunctionParameter param) {
			parameters.add(param);
		} else {
			throw new RuntimeException("Unknown variable symbol: " + symbol.getClass());
		}
	}
	
	public AbstractMachineFunction(AbstractMachine machine, AbstractMachineInstruction[] instr, Context parseContext) {
		this.machine = machine;
		this.instr = instr;
		this.parseContext = parseContext;
		this.parseContext.getDefinedSymbols()
				.stream()
				.filter(s -> s.getSymbolType() == SymbolType.variable_definition && s instanceof LocalVariableSymbol)
				.forEach(this::addSymbol);
	}
	
	public AbstractMachineFunction(AbstractMachine machine, List<AbstractSyntaxTreeNode> statements, Context parseContext) {
		this.machine = machine;
		this.parseContext = parseContext;
		parseContext.getDefinedSymbols()
				.stream()
				.filter(s -> s.getSymbolType() == SymbolType.variable_definition)
				.forEach(this::addSymbol);
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
		parseContext.getDefinedSymbols()
				.stream()
				.filter(s -> s.getSymbolType() == SymbolType.variable_definition)
				.forEach(this::addSymbol);
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

		stackFrame.arguments = new AbstractMachineValue[parameters.size()];
		for (int i = 0; i < parameters.size(); i++) {
			if (machine.registerStack.size() == 0) throw new RuntimeException("Bad stack. Invalid number of arguments passed");
			stackFrame.arguments[i] = machine.registerStack.pop();
		}
		
		return stackFrame;
	}
}
