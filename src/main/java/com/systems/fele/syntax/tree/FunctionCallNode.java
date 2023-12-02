package com.systems.fele.syntax.tree;

import java.io.PrintStream;
import java.util.List;

import com.systems.fele.machine.AbstractMachineType;
import com.systems.fele.syntax.Context;
import com.systems.fele.syntax.function.FunctionSymbol;

/**
 * Represents a function node in the AST. Hols both the callee and arguments expressions
 */
public class FunctionCallNode extends AbstractSyntaxTreeNode {

	private final List<AbstractSyntaxTreeNode> arguments;
	private final AbstractSyntaxTreeNode callee;

	public FunctionCallNode(AbstractSyntaxTreeNode callee, List<AbstractSyntaxTreeNode> arguments) {
		super(callee.sourceToken);
		this.callee = callee;
		this.arguments = arguments;
	}

	@Override
	public void printTree(PrintStream os, String indent) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Returns the function name
	 * @deprecated Use getCallee instead, as later the callee could be a compount expression
	 * @return function name
	 */
	@Deprecated
	public String getFuncitonName() {
		return callee.toString();
	}

	public AbstractSyntaxTreeNode getCallee() {
		return callee;
	}

	public List<AbstractSyntaxTreeNode> getArguments() {
		return List.copyOf(arguments);
	}
	
	@Override
	public AbstractMachineType evaluateType(Context context) {
		var symbol = context.findSymbol(getFuncitonName());

		if (symbol instanceof FunctionSymbol functionSymbol) {
			return functionSymbol.getReturnType();
		} else if (symbol == null) {
			throw new RuntimeException(getFuncitonName() + ": no such function.");
		} else {
			throw new RuntimeException(getFuncitonName() + " is not a function.");
		}
	}

}
