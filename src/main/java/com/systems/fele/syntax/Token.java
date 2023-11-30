package com.systems.fele.syntax;

public record Token(TokenKind kind, String text, int pos) {
	
	public boolean isAssignment() {
		return text.equals("=") || text.equals("=+") || text.equals("=-") || text.equals("*=") || text.equals("/=");
	}
	
}
