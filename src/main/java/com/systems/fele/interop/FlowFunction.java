package com.systems.fele.interop;

import java.util.Arrays;

import com.systems.fele.machine.AbstractMachineType;
import com.systems.fele.machine.AbstractMachineValue;
import com.systems.fele.syntax.function.FunctionSymbol;

public class FlowFunction {
    private final FunctionSymbol functionSymbol;

    public FlowFunction(FunctionSymbol functionSymbol) {
        this.functionSymbol = functionSymbol;
    }

    public AbstractMachineValue invoke(Object... arguments) {
        return invoke( Arrays.asList(arguments).stream().map(Marshaller::marshallFromJava).toArray(AbstractMachineValue[]::new) );
    }

    public AbstractMachineValue invoke(AbstractMachineValue... args) {
        var params = functionSymbol.getParameters();
        if (params.size() != args.length) {
            throw new IllegalArgumentException("Argument list size mismatch. Expected: " + params.size() + ". Got: " + args.length);
        }

        for (int i = 0; i < params.size(); i++) {
            if (!args[i].getType().equals(params.get(i).getType())) {
                throw new IllegalArgumentException("Argument %d (%s) type mismatch. Expected: %s. Got: %s".formatted(i, params.get(i).getName(), params.get(i).getType(), args[i].getType()));
            }
        }

        var amc = functionSymbol.getAbstractMachineContext();
        var registerStack = amc.getMachine().registerStack;
        for (int i = params.size()-1; i >= 0; i--) {
            registerStack.push(args[i]);
        }
        amc.executeDebug(System.out);

        if (functionSymbol.getReturnType().equals(AbstractMachineType.VOID)) {
            return null;
        } else {
            return registerStack.pop();
        }
    }
}
