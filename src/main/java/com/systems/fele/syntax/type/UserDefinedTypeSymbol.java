package com.systems.fele.syntax.type;

import java.util.List;

import com.systems.fele.machine.AbstractMachineType;
import com.systems.fele.syntax.Context;
import com.systems.fele.syntax.Symbol;
import com.systems.fele.syntax.SymbolType;

public class UserDefinedTypeSymbol extends Symbol {
	
	
	public UserDefinedTypeSymbol(String name, SymbolType symbolType, Context parentContext, List<UserDefinedTypeField> fields) {
		super(name, symbolType, parentContext);
		this.fields = fields;
	}

	private List<UserDefinedTypeField> fields;
	
	public List<UserDefinedTypeField> getFields() {
		return fields;
	}

	@Override
	public AbstractMachineType getAbstractMachineType() {
		return parentContext.getProgram().typeManager().get(name);
	}
}