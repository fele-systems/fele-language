package com.systems.fele.syntax;

import java.util.Collection;

import com.systems.fele.machine.AbstractMachineType;
import com.systems.fele.syntax.function.FunctionSymbol;

public class ParseContext implements Context {
	public enum ContextType {
		NAMESLESS_BLOCK,
		FUNCTION,
		EXTERN_FUNCTION
	}
	
	public final String contextName;
	private final Context parentContext;
	public final SymbolTable symbolTable;
	private final ContextType type;
	private Symbol itself;
	
	public ParseContext(String contextName, Context parentContext, ContextType type) {
		this.contextName = contextName;
		this.parentContext = parentContext;
		this.symbolTable = new SymbolTable();
		this.type = type;
		
	}
	
	public Symbol findSymbol(String name) {
		var symbol = symbolTable.findSymbol(name);
		if (symbol == null && parentContext != null) {
			return parentContext.findSymbol(name);
		} else {
			return symbol;
		}
	}
	
	public ContextType getType() {
		return type;
	}
	
	public int getDepth() {
		return parentContext.getDepth() + 1;
	}

	public AbstractMachineType findType(Token sourceToken) {
		return symbolTable.findSymbol(sourceToken.text()).getAbstractMachineType();
	}

	@Override
	public String getContextName() {
		return contextName;
	}

	@Override
	public Symbol defineSymbol(Symbol symbol) {
		return symbolTable.newSymbol(symbol);
	}

	@Override
	public Program getProgram() {
		return parentContext.getProgram();
	}

	@Override
	public Collection<Symbol> getDefinedSymbols() {
		return symbolTable.getSymbols();
	}

	@Override
	public Symbol getAsSymbol() {
		
		return itself;
	}

	public void setSymbol(Symbol symbol) {
		itself = symbol;
	}
}
