package com.systems.fele.syntax;

import java.util.Map;

import com.systems.fele.machine.AbstractMachineType;

public class TypeManager {
	
	public static TypeManager withBuiltins() {
		var builtins = Map.of(
				AbstractMachineType.I32.name(), AbstractMachineType.I32,
				AbstractMachineType.F32.name(), AbstractMachineType.F32,
				AbstractMachineType.TYPE.name(), AbstractMachineType.TYPE,
				AbstractMachineType.FUNCTION.name(), AbstractMachineType.FUNCTION
				);
		
		return new TypeManager(builtins);
	}
	
	private TypeManager(Map<String, AbstractMachineType> types) {
		this.types = types;
	}
	
	public AbstractMachineType get(String typeName) {
		return types.get(typeName);
	}

	private Map<String, AbstractMachineType> types;
}
