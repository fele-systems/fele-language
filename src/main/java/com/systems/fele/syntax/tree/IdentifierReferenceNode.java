package com.systems.fele.syntax.tree;

import java.io.PrintStream;

import com.systems.fele.machine.AbstractMachineType;
import com.systems.fele.syntax.Context;
import com.systems.fele.syntax.Token;

public class IdentifierReferenceNode extends AbstractSyntaxTreeNode {
	public IdentifierReferenceNode(Token sourceToken) {
		super(sourceToken);
	}

	@Override
	public void printTree(PrintStream os, String indent) {
		os.print(indent);
		os.println(sourceToken.text());
	}

	@Override
	public AbstractMachineType evaluateType(Context context) {
		var symbol = context.findSymbol(sourceToken.text());
		if (symbol == null) throw new RuntimeException("Undefined symbol: " + sourceToken.text());
		return symbol.getAbstractMachineType();
	}

	@Override
	public String toString() {
		return sourceToken.text();
	}
}
