package com.systems.fele.syntax.tree;

import java.io.PrintStream;

import com.systems.fele.machine.AbstractMachineType;
import com.systems.fele.machine.AbstractMachineValue;
import com.systems.fele.syntax.Context;
import com.systems.fele.syntax.ParseContext;
import com.systems.fele.syntax.Token;
import com.systems.fele.syntax.TokenKind;

public class NumberLiteralNode extends AbstractSyntaxTreeNode {

	public NumberLiteralNode(Token token) {
		super(token);
	}
	
	public boolean isIntegral() {
		return sourceToken.kind() == TokenKind.INTEGER_LITERAL;
	}
	
	public int parseInt() {
		assert(sourceToken.kind() == TokenKind.INTEGER_LITERAL);
		return Integer.parseInt(sourceToken.text());
	}
	
	public float parseFloat() {
		assert(sourceToken.kind() == TokenKind.FLOAT_LITERAL);
		return Float.parseFloat(sourceToken.text());
	}

	@Override
	public void printTree(PrintStream os, String indent) {
		os.print(indent);
		os.print(sourceToken.text());
		os.print('\n');
	}
	
	public AbstractMachineValue getValue() {
		return isIntegral()
				? AbstractMachineValue.newI32(parseInt())
				: AbstractMachineValue.newF32(parseFloat());
	}

	@Override
	public AbstractMachineType evaluateType(Context context) {
		return isIntegral()
				? AbstractMachineType.I32
				: AbstractMachineType.F32;
	}
	
	@Override
	public String toString() {
		return sourceToken.text();
	}
}
