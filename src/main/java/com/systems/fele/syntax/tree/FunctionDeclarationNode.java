package com.systems.fele.syntax.tree;

import java.io.PrintStream;
import java.util.List;

import com.systems.fele.machine.AbstractMachineType;
import com.systems.fele.syntax.Context;
import com.systems.fele.syntax.ParseContext;
import com.systems.fele.syntax.Token;

public class FunctionDeclarationNode extends AbstractSyntaxTreeNode {

	private AbstractSyntaxTreeNode returnType;
	private List<VariableDeclarationNode> parameters;
	private List<AbstractSyntaxTreeNode> body;

	protected FunctionDeclarationNode(Token startToken, AbstractSyntaxTreeNode returnType, List<VariableDeclarationNode> parameters, List<AbstractSyntaxTreeNode> body) {
		super(startToken);
		this.returnType = returnType;
		this.parameters = parameters;
		this.body = body;
	}

	@Override
	public void printTree(PrintStream os, String indent) {
		os.print(indent);
		
	}

	@Override
	public AbstractMachineType evaluateType(Context context) {
		return AbstractMachineType.VOID;
	}
	
}
