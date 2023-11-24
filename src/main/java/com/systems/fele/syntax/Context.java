package com.systems.fele.syntax;

import java.util.Collection;
import java.util.List;

import com.systems.fele.machine.AbstractMachineFunction;
import com.systems.fele.machine.AbstractMachineType;
import com.systems.fele.syntax.function.FunctionParameter;
import com.systems.fele.syntax.function.FunctionSymbol;
import com.systems.fele.syntax.type.UserDefinedTypeField;
import com.systems.fele.syntax.type.UserDefinedTypeSymbol;
import com.systems.fele.syntax.variable.LocalVariableSymbol;

public interface Context {
	String getContextName();
	
	Symbol findSymbol(String name);
	
	default Symbol defineFunction(String name, List<FunctionParameter> parameters, AbstractMachineType returnType, AbstractMachineFunction context) {
		return defineSymbol(new FunctionSymbol(name, SymbolType.function_definition, this, parameters, returnType, context));
	}
	
	default Symbol defineVariable(String variable, AbstractMachineType type) {
		return defineSymbol(new LocalVariableSymbol(variable, SymbolType.variable_definition, this, type));
	}
	
	default Symbol defineType(String typeName, List<UserDefinedTypeField> fields) {
		return defineSymbol(new UserDefinedTypeSymbol(typeName, SymbolType.type_definition, this, fields));
	}
	
	Symbol defineSymbol(Symbol symbol);
	
	Symbol getAsSymbol();
	
	Collection<Symbol> getDefinedSymbols();
	
	Program getProgram();

	int getDepth();
}
