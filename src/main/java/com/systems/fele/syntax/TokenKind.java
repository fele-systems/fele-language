package com.systems.fele.syntax;

public enum TokenKind {
	STRING_LITERAL,
	INTEGER_LITERAL,
	FLOAT_LITERAL,
	IDENTIFIER,
	KEYWORD,
	OPEN_PARENTHESIS,
	CLOSE_PARENTHESIS,
	OPERATOR,
	EOF, COLON, SEMICOLON, WHITESPACE, OPEN_BRACES, CLOSE_BRACES, CLOSE_BRACKETS, OPEN_BRACKETS, COMMA;
	
	public static boolean isOperand(TokenKind kind) {
		return kind == STRING_LITERAL
				|| kind == INTEGER_LITERAL
				|| kind == FLOAT_LITERAL
				|| kind == IDENTIFIER;
	}
}
