package com.systems.fele.interop;

import com.systems.fele.machine.AbstractMachineValue;

public class Marshaller {

    private Marshaller() {}

    public static AbstractMachineValue marshallFromJava(Object object) {
        if (object.getClass() == Integer.class) {
            return AbstractMachineValue.newI32((int) object);
        } else if (object.getClass() == Float.class) {
            return AbstractMachineValue.newF32((int) object);
        } else {
            throw new RuntimeException("Cannot marshall type: " + object.getClass());
        }
    }

}
