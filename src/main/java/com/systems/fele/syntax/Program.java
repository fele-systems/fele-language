package com.systems.fele.syntax;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.systems.fele.machine.AbstractMachine;
import com.systems.fele.machine.AbstractMachineFunction;
import com.systems.fele.machine.AbstractMachineType;
import com.systems.fele.machine.AbstractMachineValue;
import com.systems.fele.syntax.function.FunctionSymbol;
import com.systems.fele.syntax.type.BuiltinTypeSymbol;

public class Program {

	public Program() {
		var lib = addLibrary("stio");
		lib.defineExternalFunction("writeiln", rstack -> {
			var arg0 = rstack.pop().asInt();
			System.out.println(arg0);
			return AbstractMachineValue.NULL;
		}, AbstractMachineType.VOID, AbstractMachineType.I32);
		
		lib.defineExternalFunction("writefln", rstack -> {
			var arg0 = rstack.pop().asFloat();
			System.out.println(arg0);
			return AbstractMachineValue.NULL;
		}, AbstractMachineType.VOID, AbstractMachineType.F32);
	}
	
	List<CompileUnit> compileUnits = new ArrayList<>();
	TypeManager typeManager = TypeManager.withBuiltins();
	AbstractMachine machine = new AbstractMachine();
	Context builtinContext = new Context() {

		@Override
		public String getContextName() {
			return "compiler_builtins";
		}

		@Override
		public Symbol findSymbol(String name) {
			throw new UnsupportedOperationException("Unimplemented method 'findSymbol'");
		}

		@Override
		public Symbol defineSymbol(Symbol symbol) {
			throw new UnsupportedOperationException("Unimplemented method 'defineSymbol'");
		}

		@Override
		public Symbol getAsSymbol() {
			throw new UnsupportedOperationException("Unimplemented method 'getAsSymbol'");
		}

		@Override
		public Collection<Symbol> getDefinedSymbols() {
			throw new UnsupportedOperationException("Unimplemented method 'getDefinedSymbols'");
		}

		@Override
		public Program getProgram() {
			throw new UnsupportedOperationException("Unimplemented method 'getProgram'");
		}

		@Override
		public int getDepth() {
			throw new UnsupportedOperationException("Unimplemented method 'getDepth'");
		}
		
	};
	
	public AbstractMachineFunction getMainFunction() {
		return findFunction("main").getAbstractMachineContext();
	}

	public FunctionSymbol findFunction(String name) {
		return (FunctionSymbol) findGlobalSymbol(name);
	}

	public Symbol findGlobalSymbol(String name) {
		for (var cu : compileUnits) {
			var s = cu.symbolTable.findSymbol(name);
			if (s == null) continue;

			return s;
		}

		var asType = typeManager.get(name);
		if (asType != null) {
			return new BuiltinTypeSymbol(asType, builtinContext);
		}

		return null;
	}

	
	public TypeManager typeManager() {
		return typeManager;
	}
	
	public void addCompileUnit(CompileUnit unit) {
		compileUnits.add(unit);
	}
	
	public Library addLibrary(String libraryName) {
		var lib = new CompileUnit(libraryName, this);
		compileUnits.add(lib);
		return new Library(lib);
	}

	public AbstractMachine getMachine() {
		return machine;
	}

	public void dump(PrintStream stream) {		
		for (var cu : compileUnits) {
			stream.println("#" + cu.fileName);
			for (var symbols : cu.symbolTable.getSymbols()) {
				if (symbols instanceof FunctionSymbol f) {
					stream.println(f.name + ":");
					f.getAbstractMachineContext().disassemble(stream);
				}
			}
		}
	}
}
