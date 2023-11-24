package com.systems.fele.syntax.tree;

import java.io.PrintStream;
import java.util.List;

import com.systems.fele.machine.AbstractMachineType;
import com.systems.fele.syntax.Context;
import com.systems.fele.syntax.Token;
import com.systems.fele.syntax.function.FunctionSymbol;

public class FunctionCallNode extends AbstractSyntaxTreeNode {

	private List<AbstractSyntaxTreeNode> arguments;

	public FunctionCallNode(Token sourceToken, List<AbstractSyntaxTreeNode> arguments) {
		super(sourceToken);
		this.arguments = arguments;
	}

	@Override
	public void printTree(PrintStream os, String indent) {
		// TODO Auto-generated method stub

	}
	
	public String getFuncitonName() {
		return getSourceToken().text();
	}

	public List<AbstractSyntaxTreeNode> getArguments() {
		return List.copyOf(arguments);
	}
	
	@Override
	public AbstractMachineType evaluateType(Context context) {
		return switch(context.findSymbol(getFuncitonName())) {
			case FunctionSymbol f -> f.getReturnType();
			case null -> throw new RuntimeException(getFuncitonName() + ": no such function.");
			default -> throw new RuntimeException(getFuncitonName() + " is not a function.");
		};
	}

}
