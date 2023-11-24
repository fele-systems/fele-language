package com.systems.fele.syntax.tree;

import java.io.PrintStream;

import com.systems.fele.machine.AbstractMachineType;
import com.systems.fele.syntax.Context;
import com.systems.fele.syntax.Token;

public class ReturnNode extends AbstractSyntaxTreeNode {
	AbstractSyntaxTreeNode expression;
	
	public ReturnNode(Token sourceToken, AbstractSyntaxTreeNode expression) {
		super(sourceToken);
		this.expression = expression;
	}

	@Override
	public void printTree(PrintStream os, String indent) {
		os.print(indent);
		os.print("ret");
		expression.printTree(os, indent);
	}

	@Override
	public AbstractMachineType evaluateType(Context context) {
		return expression.evaluateType(context);
	}
	
	public AbstractSyntaxTreeNode getExpression() {
		return expression;
	}

	
}
