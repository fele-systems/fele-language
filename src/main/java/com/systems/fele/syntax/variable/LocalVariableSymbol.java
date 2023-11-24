package com.systems.fele.syntax.variable;

import com.systems.fele.machine.AbstractMachineType;
import com.systems.fele.syntax.Context;
import com.systems.fele.syntax.Symbol;
import com.systems.fele.syntax.SymbolType;

public class LocalVariableSymbol extends Symbol {

	private AbstractMachineType type;

	public LocalVariableSymbol(String name, SymbolType symbolType, Context parentContext, AbstractMachineType type) {
		super(name, symbolType, parentContext);
		this.type = type;
	}

	@Override
	public AbstractMachineType getAbstractMachineType() {
		return type;
	}
	
	
		
}