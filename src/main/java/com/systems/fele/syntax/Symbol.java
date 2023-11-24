package com.systems.fele.syntax;

import com.systems.fele.machine.AbstractMachineType;

public abstract class Symbol {
	public Symbol(String name, SymbolType symbolType, Context parentContext) {
		this.name = name;
		this.symbolType = symbolType;
		this.parentContext = parentContext;
	}

	protected String name;
	protected SymbolType symbolType;
	protected Context parentContext;
	
	public String getName() {
		return name;
	}
	
	public SymbolType getSymbolType() {
		return symbolType;
	}
	
	public Context getParentContext() {
		return parentContext;
	}
	
	public abstract AbstractMachineType getAbstractMachineType();
	

}
