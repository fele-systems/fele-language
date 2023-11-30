package com.systems.fele.syntax.tree;

import java.io.PrintStream;

import com.systems.fele.machine.AbstractMachineType;
import com.systems.fele.syntax.Context;
import com.systems.fele.syntax.Token;

public class ParenthesisNode extends AbstractSyntaxTreeNode {

	private final AbstractSyntaxTreeNode innerNode;

	public AbstractSyntaxTreeNode getInnerNode() {
		return innerNode;
	}

	public ParenthesisNode(AbstractSyntaxTreeNode innerNode, Token sourceToken) {
		super(sourceToken);
		this.innerNode = innerNode;
	}

	@Override
	public void printTree(PrintStream os, String indent) {
		os.print(indent);
		os.println("()");
		innerNode.printTree(os, indent + "  ");
	}

	@Override
	public AbstractMachineType evaluateType(Context context) {
		return innerNode.evaluateType(context);
	}

}
