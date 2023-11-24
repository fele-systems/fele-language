package com.systems.fele.syntax.function;

import java.util.List;

import com.systems.fele.machine.AbstractMachineFunction;
import com.systems.fele.machine.AbstractMachineType;
import com.systems.fele.syntax.Context;
import com.systems.fele.syntax.Symbol;
import com.systems.fele.syntax.SymbolType;

public class FunctionSymbol extends Symbol {
	 
	private AbstractMachineFunction amContext;

	public FunctionSymbol(String name, SymbolType symbolType, Context parentContext, List<FunctionParameter> parameters,
			AbstractMachineType returnType, AbstractMachineFunction amContext) {
		super(name, symbolType, parentContext);
		this.parameters = parameters;
		this.returnType = returnType;
		this.amContext = amContext;
	}

	List<FunctionParameter> parameters;
	AbstractMachineType returnType;
	
	public List<FunctionParameter> getParameters() {
		return parameters;
	}
	public AbstractMachineType getReturnType() {
		return returnType;
	}
	@Override
	public AbstractMachineType getAbstractMachineType() {
		return AbstractMachineType.FUNCTION;
	}
	
	public AbstractMachineFunction getAbstractMachineContext() {
		return amContext;
	}
	
}
	