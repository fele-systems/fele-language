package com.systems.fele.syntax;

import java.io.PrintStream;

import com.systems.fele.machine.AbstractMachineValue;
import com.systems.fele.syntax.tree.AbstractSyntaxTreeNode;

public class AbstractSyntaxTree {
	
	public final AbstractSyntaxTreeNode root;
	public final Context globalContext;
	
	public AbstractSyntaxTree(AbstractSyntaxTreeNode root, Context parseContext) {
		this.root = root;
		this.globalContext = parseContext;
	}
	
	public void printTree(PrintStream os) {
		root.printTree(os, "");
	}
}
