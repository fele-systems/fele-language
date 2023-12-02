package com.systems.fele.syntax;

import java.io.Serializable;

public record Token(TokenKind kind, String text, int pos) implements Serializable {
	
	public boolean isAssignment() {
		return text.equals("=") || text.equals("=+") || text.equals("=-") || text.equals("*=") || text.equals("/=");
	}
	
}
