package com.systems.fele.syntax.tree;

import java.io.PrintStream;

import com.systems.fele.machine.AbstractMachineType;
import com.systems.fele.syntax.Context;
import com.systems.fele.syntax.Token;

public class VariableDeclarationNode extends AbstractSyntaxTreeNode {

	private Token paramName;
	private AbstractSyntaxTreeNode initialValueExpression;

	protected VariableDeclarationNode(Token paramType, Token paramName, AbstractSyntaxTreeNode initialValueExpression) {
		super(paramType);
		this.paramName = paramName;
		this.initialValueExpression = initialValueExpression;
	}

	@Override
	public void printTree(PrintStream os, String indent) {
		os.print(indent);
	}
	
	public String getVariableName() {
		return paramName.text();
	}

	@Override
	public AbstractMachineType evaluateType(Context context) {
		if (sourceToken.text().equals("auto")) {
			return initialValueExpression.evaluateType(context);
		} else {
			return context.getProgram().typeManager().get(sourceToken.text());
		}
	}

}
