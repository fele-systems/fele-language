package com.systems.fele.syntax.type;

import com.systems.fele.machine.AbstractMachineType;
import com.systems.fele.syntax.Context;
import com.systems.fele.syntax.Symbol;
import com.systems.fele.syntax.SymbolType;

public class BuiltinTypeSymbol extends Symbol {

    private final AbstractMachineType type;

    public BuiltinTypeSymbol(AbstractMachineType type, Context parentContext) {
        super(type.name(), SymbolType.type_definition, parentContext);
        this.type = type;
    }

    @Override
    public AbstractMachineType getAbstractMachineType() {
        return type;
    }
    
}
