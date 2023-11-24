package com.systems.fele.syntax.tree;

import java.io.PrintStream;
import java.util.Arrays;

import com.systems.fele.machine.AbstractMachineType;
import com.systems.fele.syntax.Context;
import com.systems.fele.syntax.Token;

public class BinaryOperatorNode extends AbstractSyntaxTreeNode {
	
	public enum OperatorType {
		PLUS("+", 0),
		MINUS("-", 0),
		STAR("*", 1),
		SLASH("/", 1);
		
		private final String symbol;
		private final int precedence;

		private OperatorType(String symbol, int precedence) {
			this.symbol = symbol;
			this.precedence = precedence;
		}

		public String getSymbol() {
			return symbol;
		}

		public int getPrecedence() {
			return precedence;
		}
		
		public static OperatorType fromSymbol(String symbol) {
			return Arrays.asList(OperatorType.values())
				.stream()
				.filter(type -> type.getSymbol().equals(symbol))
				.findFirst()
				.orElseThrow();
		}
	}
	
	private final AbstractSyntaxTreeNode lhs;
	private final AbstractSyntaxTreeNode rhs;
	private final OperatorType type;
	
	public BinaryOperatorNode(AbstractSyntaxTreeNode lhs, AbstractSyntaxTreeNode rhs, Token sourceToken) {
		super(sourceToken);
		this.lhs = lhs;
		this.rhs = rhs;
		this.type = OperatorType.fromSymbol(sourceToken.text());
	}
	
	public AbstractSyntaxTreeNode getLhs() {
		return lhs;
	}
	public AbstractSyntaxTreeNode getRhs() {
		return rhs;
	}
	public OperatorType getOperatorType() {
		return type;
	}

	@Override
	public AbstractMachineType evaluateType(Context context) {
		return AbstractMachineType.promotePrimitives(lhs.evaluateType(context), rhs.evaluateType(context));
	}
	
	@Override
	public void printTree(PrintStream os, String indent) {
		os.print(indent);
		os.print(type.symbol);
		os.print('\n');
		lhs.printTree(os, indent + "  ");
		rhs.printTree(os, indent + "  ");
	}
	
	private int applyOnInt(int lhs, int rhs) {
		return switch(type) {
			case MINUS -> lhs - rhs;
			case PLUS -> lhs + rhs;
			case SLASH -> lhs / rhs;
			case STAR -> lhs * rhs;
		};
	}
	
	private float applyOnFloat(float lhs, float rhs) {
		return switch(type) {
			case MINUS -> lhs - rhs;
			case PLUS -> lhs + rhs;
			case SLASH -> lhs / rhs;
			case STAR -> lhs * rhs;
		};
	}

}
