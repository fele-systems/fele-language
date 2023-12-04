package com.systems.fele.syntax;

import java.util.Collection;

public class CompileUnit implements Context {

	public CompileUnit(String fileName, Program program) {
		this.fileName = fileName;
		this.program = program;
		program.addCompileUnit(this);
	}

	String fileName;
	Program program;
	SymbolTable symbolTable = new SymbolTable();
	
	@Override
	public String getContextName() {
		return "global";
	}

	@Override
	public Symbol findSymbol(String name) {
		var symbol = symbolTable.findSymbol(name);
		if (symbol == null) {
			return program.findGlobalSymbol(name);
		} else {
			return symbol;
		}
	}

	@Override
	public Symbol defineSymbol(Symbol symbol) {
		return symbolTable.newSymbol(symbol);
	}

	@Override
	public Program getProgram() {
		return program;
	}

	@Override
	public int getDepth() {
		return 0;
	}

	@Override
	public Collection<Symbol> getDefinedSymbols() {
		return symbolTable.getSymbols();
	}

	@Override
	public Symbol getAsSymbol() {
		return null;
	}
	
	@Override
	public String toString() {
		return getContextName();
	}
	
	
}
