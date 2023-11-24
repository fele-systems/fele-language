package com.systems.fele.syntax.tree;

import java.io.PrintStream;

import com.systems.fele.machine.AbstractMachineType;
import com.systems.fele.syntax.Context;
import com.systems.fele.syntax.Token;

public class AssignmentNode extends AbstractSyntaxTreeNode {

	private AbstractSyntaxTreeNode assignTo;
	private AbstractSyntaxTreeNode expression;

	public AssignmentNode(AbstractSyntaxTreeNode assignTo, Token operatorToken, AbstractSyntaxTreeNode expression) {
		super(operatorToken);
		this.assignTo = assignTo;
		this.expression = expression;
	}

	public AbstractSyntaxTreeNode getLhs() {
		return assignTo;
	}
	
	@Override
	public void printTree(PrintStream os, String indent) {
		os.print(indent);
		os.println(sourceToken.text());
		assignTo.printTree(os, indent+"  ");
		expression.printTree(os, indent+"  ");
	}

	@Override
	public AbstractMachineType evaluateType(Context context) {
		return expression.evaluateType(context);
	}
	
	@Override
	public String toString() {
		return assignTo.toString() + " = " + expression.toString();
	}

	public AbstractSyntaxTreeNode getRhs() {
		return expression;
	}

}
