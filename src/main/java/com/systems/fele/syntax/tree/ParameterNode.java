package com.systems.fele.syntax.tree;

import java.io.PrintStream;

import com.systems.fele.machine.AbstractMachineType;
import com.systems.fele.syntax.Context;
import com.systems.fele.syntax.Token;

public class ParameterNode extends AbstractSyntaxTreeNode {
    
    TypeExpressionNode type;
    Token name;

    public ParameterNode(TypeExpressionNode type, Token name) {
        super(type.sourceToken);
        this.type = type;
        this.name = name;
    }

    @Override
    public void printTree(PrintStream os, String indent) {
        os.print(indent);
        type.printTree(os, "");
        os.print(' ');
        os.print(name.text());
    }

    @Override
    public AbstractMachineType evaluateType(Context context) {
        return AbstractMachineType.VOID;
    }

    public TypeExpressionNode getType() {
        return type;
    }

    public String getName() {
        return name.text();
    }

    

}
