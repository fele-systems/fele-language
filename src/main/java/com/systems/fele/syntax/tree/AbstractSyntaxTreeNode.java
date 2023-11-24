package com.systems.fele.syntax.tree;

import java.io.PrintStream;

import com.systems.fele.machine.AbstractMachineType;
import com.systems.fele.syntax.Context;
import com.systems.fele.syntax.Token;

public abstract class AbstractSyntaxTreeNode {
	protected final Token sourceToken;

	protected AbstractSyntaxTreeNode(Token sourceToken) {
		this.sourceToken = sourceToken;
	}
	
	public Token getSourceToken() {
		return sourceToken;
	}
	
	public abstract void printTree(PrintStream os, String indent);
	
	public abstract AbstractMachineType evaluateType(Context context);
}
