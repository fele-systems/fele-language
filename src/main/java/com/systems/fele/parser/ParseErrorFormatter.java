package com.systems.fele.parser;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

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

    public void printRelatedLines(Writer writer, int linesUp, int linesDown) {
        try {
            // Width of the line numbers column
            final var lineNoPadding = String.valueOf(tokenLine + linesUp).length();

            // Calculates the current token line's start and end
            final int start = getLineStart(highlightedToken.pos());
            final int end = getLineEnd(highlightedToken.pos());
            
            // We need to print the lines in the reverse order they were found, so we put them onto a list
            final List<String> previousLines = new ArrayList<>();
            int prevLineStart = start - 1;
            while (linesUp > 0) {
                final int prevLineEnd = prevLineStart-1;
                if (prevLineEnd < 0) break;
                prevLineStart = getLineStart(prevLineStart-1);
                previousLines.add(sourceCode.substring(prevLineStart, prevLineEnd));
                linesUp--;
            }

            // Now print them
            for (int i = 0; i < previousLines.size(); i++) {
                printLine(tokenLine + i - previousLines.size(),
                        lineNoPadding,
                        previousLines.get(previousLines.size() - i - 1),
                        writer);
            }

            // Print the actual token line
            final String line = sourceCode.substring(start, end);
            final int positionInLine = highlightedToken.pos() - start;
            printLine(tokenLine,
                    lineNoPadding,
                    line,
                    writer);
            
            // Highlights the acused token
            // TODO:Medium Normalize tabulations
            for (int i = 0; i < positionInLine + lineNoPadding + 1; i++) {
                writer.append('~');
            }
            
            writer.append('^');
            writer.append('\n');
            
            // Now, print the lower lines
            int nextLineStart = end + 1;
            int currentNextLine = tokenLine + 1;
            while (linesDown > 0) {
                if (nextLineStart >= sourceCode.length()) break;
                int nextLineEnd = getLineEnd(nextLineStart);
                if (nextLineEnd < 0) nextLineEnd = sourceCode.length();
                printLine(currentNextLine++,
                        lineNoPadding,
                        sourceCode.substring(nextLineStart, nextLineEnd),
                        writer);
                nextLineStart = nextLineEnd + 1;
                linesDown--;
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while formatting source code for error reporting", e);
        }
    }

    private void printLine(int lineNo, int lineNoPadding, String line, Writer writer) throws IOException {
        var lineNoStr = String.valueOf(lineNo);
        var paddedCharNumber = lineNoPadding - lineNoStr.length();
        for (int i = 0; i < paddedCharNumber; i++) {
            writer.append(' ');
        }
        
        writer.append(lineNoStr);
        writer.append('|');
        writer.append(line);
        writer.append('\n');
        writer.flush();
    }

    public String getMessage() {
        StringWriter writer = new StringWriter();
        printRelatedLines(writer, 3, 3);
        return writer.toString();
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
