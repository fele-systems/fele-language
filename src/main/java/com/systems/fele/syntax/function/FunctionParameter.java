package com.systems.fele.syntax.function;

import com.systems.fele.machine.AbstractMachineType;
import com.systems.fele.syntax.Context;
import com.systems.fele.syntax.Symbol;
import com.systems.fele.syntax.SymbolType;

public class FunctionParameter extends Symbol {

	public FunctionParameter(int index, String name, AbstractMachineType type, Context context) {
		super(name, SymbolType.variable_definition, context);
		this.index = index;
		this.type = type;
	}

	private final int index;
	private final AbstractMachineType type;

	public int getIndex() {
		return index;
	}

	public AbstractMachineType getType() {
		return type;
	}

	@Override
	public AbstractMachineType getAbstractMachineType() {
		return getType();
	}
}
