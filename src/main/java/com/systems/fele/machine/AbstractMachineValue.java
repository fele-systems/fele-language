package com.systems.fele.machine;

public class AbstractMachineValue {
	
	public AbstractMachineValue(Object byRef, long byVal, AbstractMachineType type) {
		this.byRef = byRef;
		this.byVal = byVal;
		this.type = type;
	}
	
	Object byRef;
	long byVal;
	AbstractMachineType type;
	
	public AbstractMachineType getType() {
		return type;
	}
	
	public int asInt() {
		return (int) byVal;
	}
	
	public byte asByte() {
		return (byte) byVal;
	}
	
	public float asFloat() {
		return Float.intBitsToFloat((int)byVal);
	}
	
	public boolean isNull() {
		return false;
	}
	
	public boolean isRef() {
		return isNull() || byRef != null;
	}
	
	public String toString() {
		return isRef()
				? "Ref " + byRef.toString()
				: "Val 0x" + Long.toHexString(byVal);
	}
	
	public static AbstractMachineValue newI8(byte v) {
		return new AbstractMachineValue(null, v, AbstractMachineType.I8);
	}
	
	public static AbstractMachineValue newI32(int v) {
		return new AbstractMachineValue(null, v, AbstractMachineType.I32);
	}
	
	public static AbstractMachineValue newF32(float v) {
		return new AbstractMachineValue(null, Integer.toUnsignedLong(Float.floatToIntBits(v)), AbstractMachineType.F32);
	}
	
	public static final AbstractMachineValue NULL = new AbstractMachineValue(null, 0, null) {
		@Override
		public boolean isNull() {
			return true;
		}
		@Override
		public String toString() {
			return "nullptr";
		}
	};

}
