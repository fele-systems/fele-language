package com.systems.fele.syntax;

public class Lexer {
	private final String source;
	private int i = 0;
	
	public Lexer(String source) {
		this.source = source;
	}
	
	public Token nextToken() {
		if (i >= source.length()) return new Token(TokenKind.EOF, null, -1);
		char currentChar = source.charAt(i++);
		
		if (Character.isJavaIdentifierStart(currentChar)) {
			int start = i-1;
			for ( ; i < source.length() && Character.isJavaIdentifierPart(source.charAt(i)) ; i++ );
			var txt = source.substring(start, i);
			return new Token(switch(txt) {
			case "ret", "if", "else", "let" -> TokenKind.KEYWORD;
			default -> TokenKind.IDENTIFIER;
			}, txt, start);
		} else if (Character.isDigit(currentChar)) {
			int start = i-1;
			for ( ; i < source.length() && Character.isDigit(source.charAt(i)) ; i++ );
			
			if (i >= source.length() || source.charAt(i) != '.') {
				return new Token(TokenKind.INTEGER_LITERAL, source.substring(start, i), start);
			} else {
				i++; // skip the point
				for ( ; i < source.length() && Character.isDigit(source.charAt(i)) ; i++ );
				return new Token(TokenKind.FLOAT_LITERAL, source.substring(start, i), start);
			}
		} else if (Character.isWhitespace(currentChar)) {
			int start = i-1;
			for ( ; i < source.length() && Character.isWhitespace(source.charAt(i)) ; i++ );
			return new Token(TokenKind.WHITESPACE, source.substring(start, i), start);
		} else {
			return switch (currentChar) {
			case '+', '-', '/', '*', '=' -> new Token(TokenKind.OPERATOR, String.valueOf(currentChar), i-1);
			case ')' -> new Token(TokenKind.CLOSE_PARENTHESIS, String.valueOf(currentChar), i-1);
			case '(' -> new Token(TokenKind.OPEN_PARENTHESIS, String.valueOf(currentChar), i-1);
			case ']' -> new Token(TokenKind.CLOSE_BRACKETS, String.valueOf(currentChar), i-1);
			case '[' -> new Token(TokenKind.OPEN_BRACKETS, String.valueOf(currentChar), i-1);
			case '}' -> new Token(TokenKind.CLOSE_BRACES, String.valueOf(currentChar), i-1);
			case '{' -> new Token(TokenKind.OPEN_BRACES, String.valueOf(currentChar), i-1);
			case ':' -> new Token(TokenKind.COLON, String.valueOf(currentChar), i-1);
			case ';' -> new Token(TokenKind.SEMICOLON, String.valueOf(currentChar), i-1);
			default -> throw new RuntimeException("Unknown token: " + currentChar);
			};
		}
	}
	
}
