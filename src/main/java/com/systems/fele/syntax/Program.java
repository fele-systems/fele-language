package com.systems.fele.syntax;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.systems.fele.machine.AbstractMachine;
import com.systems.fele.machine.AbstractMachineFunction;
import com.systems.fele.machine.AbstractMachineType;
import com.systems.fele.machine.AbstractMachineValue;
import com.systems.fele.syntax.function.FunctionSymbol;

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

	
	public AbstractMachineFunction getMainFunction() {
		for (var cu : compileUnits) {
			var main = cu.findSymbol("main");
			if (main == null) continue;
			
			if (main instanceof FunctionSymbol f) {
				return f.getAbstractMachineContext();
			} else {
				continue;
			}
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
