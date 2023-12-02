package com.systems.fele.parser;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import com.systems.fele.syntax.Token;

public class ParseErrorFormatter {
    
    private final String sourceCode;
    private final Token highlightedToken;
    private final int tokenLine;

    public ParseErrorFormatter(String sourceCode, Token highlightedToken) {
        this.sourceCode = sourceCode;
        this.highlightedToken = highlightedToken;
        this.tokenLine = getTokenLine();
    }

    public void printRelatedLines(PrintStream printTo) {
        int start = getLineStart(highlightedToken.pos());
        int end = getLineEnd(highlightedToken.pos());
        String line = sourceCode.substring(start, end);
        
        String lineNoStr = String.valueOf(tokenLine);
        int positionInLine = highlightedToken.pos() - start + 2 + lineNoStr.length();
        
        printTo.print(' ');
        printTo.print(lineNoStr);
        printTo.print('|');
        printTo.println(line);

        printTo.print(' ');
        printTo.print(lineNoStr);
        printTo.print('|');
        for (int i = 0; i < positionInLine; i++) {
            printTo.print(' ');
        }
        // FIXME:Low This pointer needs adjusting. Some time goes to ealy sometimes to late
        printTo.print('^');
    }

    public String getMessage() {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8)) {
            printRelatedLines(ps);
        }
        return baos.toString(StandardCharsets.UTF_8);
    }

    private int getTokenLine() {
        int line = 1;
        for (int i = 0; i < highlightedToken.pos(); i++) {
            if (sourceCode.charAt(i) == '\n') line++;
        }
        return line;
    }

    private int getLineStart(int pos) {
        return sourceCode.lastIndexOf('\n', pos-1)+1;
    }

    private int getLineEnd(int pos) {
        return sourceCode.indexOf('\n', pos+1);
    }

}
