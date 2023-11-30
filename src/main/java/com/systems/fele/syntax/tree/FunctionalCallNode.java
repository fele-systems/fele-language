package com.systems.fele.syntax.tree;

import java.io.PrintStream;
import java.util.List;

import com.systems.fele.machine.AbstractMachineType;
import com.systems.fele.syntax.Context;
import com.systems.fele.syntax.Symbol;
import com.systems.fele.syntax.Token;
import com.systems.fele.syntax.function.FunctionSymbol;

public class FunctionalCallNode extends AbstractSyntaxTreeNode {

	private final List<AbstractSyntaxTreeNode> arguments;
	
	protected FunctionalCallNode(Token sourceToken, List<AbstractSyntaxTreeNode> arguments) {
		super(sourceToken);
		this.arguments = arguments;
	}

	@Override
	public void printTree(PrintStream os, String indent) {
		
	}

	@Override
	public AbstractMachineType evaluateType(Context context) {
		return switch(context.findSymbol(sourceToken.text())) {
			case FunctionSymbol f -> f.getReturnType();
			case Symbol s -> throw new RuntimeException("Calling a non function identifier. Expected: function. Got: " + s.getSymbolType());
		};
	}

	public List<AbstractSyntaxTreeNode> getArguments() {
		return arguments;
	}
	
	
}
