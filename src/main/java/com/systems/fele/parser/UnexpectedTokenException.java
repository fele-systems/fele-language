package com.systems.fele.parser;

import java.io.StringWriter;

import com.systems.fele.syntax.Token;
import com.systems.fele.syntax.TokenKind;

public class UnexpectedTokenException extends ParseException {
    private final String source;

    public UnexpectedTokenException(String message, String source, Token badToken) {
        super(message, badToken);
        this.source = source;
    }

    public UnexpectedTokenException(TokenKind expected, String source, Token badToken) {
        super("Expected " + expected + " during parse.", badToken);
        this.source = source;
    }

    public UnexpectedTokenException(String source, Token badToken) {
        super("Unexpected token during parse.", badToken);
        this.source = source;
    }

    @Override
    public String getMessage() {
        return getMessage(3, 2);
    }

    public String getMessage(int linesUp, int linesDown) {
        var sb = new StringWriter();
        sb.append(super.getMessage());
        sb.append(". Got: ");
        sb.append(getBadToken().toString());
        sb.append(". Parsing: \n");
        new ParseErrorFormatter(source, getBadToken()).printRelatedLines(sb, linesUp, linesDown);
        return sb.toString();
    }

    public String getSource() {
        return source;
    }
}
