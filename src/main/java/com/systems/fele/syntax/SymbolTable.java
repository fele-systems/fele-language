package com.systems.fele.syntax;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
	private Map<String, Symbol> symbols = new HashMap<>();
	
	public Symbol newSymbol(Symbol symbol) {
		symbols.put(symbol.name, symbol);
	 	return symbol;
	}
	
	public Symbol findSymbol(String name) {
		return symbols.get(name);
	}

	public Collection<Symbol> getSymbols() {
		return symbols.values();
	}
}
