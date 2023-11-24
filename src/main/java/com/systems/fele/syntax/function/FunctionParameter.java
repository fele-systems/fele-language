package com.systems.fele.syntax.function;

import com.systems.fele.machine.AbstractMachineType;

public class FunctionParameter {

	public FunctionParameter(int index, String name, AbstractMachineType type) {
		super();
		this.index = index;
		this.name = name;
		this.type = type;
	}
	int index;
	String name;
	AbstractMachineType type;
	public int getIndex() {
		return index;
	}
	public String getName() {
		return name;
	}
	public AbstractMachineType getType() {
		return type;
	}
	
	
}
