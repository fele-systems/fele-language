package com.systems.fele.syntax.tree;

import java.io.PrintStream;

import com.systems.fele.machine.AbstractMachineType;
import com.systems.fele.syntax.Context;
import com.systems.fele.syntax.SymbolType;
import com.systems.fele.syntax.Token;

public class TypeExpressionNode extends AbstractSyntaxTreeNode {
    public enum Modifier {
        NONE,
        ARRAY
    }

    private final Modifier modifier;

    public TypeExpressionNode(Token sourceToken, Modifier modifier) {
        super(sourceToken);
        this.modifier = modifier;
    }

    public Modifier getModifier() {
        return modifier;
    }

    @Override
    public void printTree(PrintStream os, String indent) {
        os.print(indent);
        os.print(sourceToken.text());
        if (modifier == Modifier.ARRAY) os.print("[]");
    }

    @Override
    public AbstractMachineType evaluateType(Context context) {
        var symbol = context.findSymbol(getSourceToken().text());
        if (symbol == null) {
            throw new RuntimeException(symbol.getName() + " does not exist.");
        } else if (symbol.getSymbolType() != SymbolType.type_definition) {
            throw new RuntimeException(symbol.getName() + " does not name a type. It is a " + symbol.getSymbolType());
        } else {
            return symbol.getAbstractMachineType();
        }
    }    
}
