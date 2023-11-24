package com.systems.fele.machine;

public record AbstractMachineType(
		int size,
		String name,
		int flags,
		InstructionType instrType,
		Object extraData
		) {
	
	public boolean isUserDefined() {
		return (flags & USER_DEFINED) > 0;
	}
	
	public boolean isIntegral() {
		return (flags & INTEGRAL) > 0;
	}
	
	public boolean isArray() {
		return (flags & ARRAY) > 0;
	}
	
	public AbstractMachineType asArray() {
		if (isArray()) return this;
		
		return new AbstractMachineType(8, name, flags | ARRAY, InstructionType._Ref, this);
	}
	
	public static final int INTEGRAL = 1;
	public static final int FLOATING = 2;
	public static final int UNSIGNED = 4;
	public static final int USER_DEFINED = 8;
	public static final int FUNCTIONAL = 16;
	public static final int ARRAY = 32;
	
	public static final AbstractMachineType CHAR = new AbstractMachineType(1, "Char", INTEGRAL, InstructionType._I8, null);
	public static final AbstractMachineType I8 = new AbstractMachineType(1, "I8", INTEGRAL, InstructionType._I8, null);
	public static final AbstractMachineType I32 = new AbstractMachineType(4, "I32", INTEGRAL, InstructionType._I32, null);
	public static final AbstractMachineType F32 = new AbstractMachineType(4, "F32", FLOATING, InstructionType._F32, null);
	public static final AbstractMachineType TYPE = new AbstractMachineType(0, "Type", 0, null, null);
	public static final AbstractMachineType FUNCTION = new AbstractMachineType(0, "Function", FUNCTIONAL, null, null);
	public static final AbstractMachineType VOID = null;
	
	public static AbstractMachineType promotePrimitives(AbstractMachineType t0, AbstractMachineType t1) {
		assert (!t0.isUserDefined() && !t1.isUserDefined()) : "Types must be primitive";
		
		if (t0 == t1) {
			return t0;
		}
		
		if (t0.isUserDefined() || t1.isUserDefined()) {
			throw new RuntimeException("Could not find user conversion operator for types %s or %s".formatted(t0, t1));
		}
		
		final int desiredSize = Math.max(t0.size, t1.size);
		var newT0 = promoteSize(t0, desiredSize);
		var newT1 = promoteSize(t1, desiredSize);
		
		if (!newT0.isIntegral() || !newT1.isIntegral()) {
			newT0 = promoteToFloat(newT0);
			newT1 = promoteToFloat(newT1);
		}
		
		assert(newT0 == newT1);
		return newT0;
	}
	
	public static AbstractMachineType promoteSize(AbstractMachineType type, int desiredSize) {
		assert (!type.isUserDefined()) : "Type must be primitive";
		if (type.size == desiredSize) {
			return type;
		} else {
			throw new RuntimeException("Could not find a promotion for type %s to size %s".formatted(type, desiredSize));
		}
	}
	
	public static AbstractMachineType promoteToFloat(AbstractMachineType type) {
		assert (!type.isUserDefined()) : "Type must be primitive";
		if (!type.isIntegral()) {
			return type;
		}
		
		if (type == I32) return F32;
		
		throw new RuntimeException("Could not find a promotion for type %s float".formatted(type));
	}
}
