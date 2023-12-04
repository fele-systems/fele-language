package com.systems.fele.syntax.tree;

import java.io.PrintStream;
import java.util.List;

import com.systems.fele.machine.AbstractMachineType;
import com.systems.fele.syntax.Context;
import com.systems.fele.syntax.Token;
import com.systems.fele.syntax.function.FunctionSymbol;

@Deprecated
public class FunctionalCallNode extends AbstractSyntaxTreeNode {

	private final List<AbstractSyntaxTreeNode> arguments;
	
	protected FunctionalCallNode(Token sourceToken, List<AbstractSyntaxTreeNode> arguments) {
		super(sourceToken);
		this.arguments = arguments;
	}

	@Override
	public void printTree(PrintStream os, String indent) {
		os.print(indent);
	}

	@Override
	public AbstractMachineType evaluateType(Context context) {
		var symbol = context.findSymbol(sourceToken.text());

		if (symbol instanceof FunctionSymbol functionSymbol) {
			return functionSymbol.getReturnType();
		} else {
			throw new RuntimeException("Calling a non function identifier. Expected: function. Got: " + symbol.getSymbolType());
		}
	}

	public List<AbstractSyntaxTreeNode> getArguments() {
		return arguments;
	}
	
	
}
