package com.systems.fele.parser;

import com.systems.fele.syntax.Token;

public class ParseException extends RuntimeException {
    private final Token badToken;

    public ParseException(String message, Token badToken) {
        super(message);
        this.badToken = badToken;
    }

    public Token getBadToken() {
        return badToken;
    }
}
