package com.systems.fele.machine;

public class RegistersStack {
	private final AbstractMachineValue[] registers = new AbstractMachineValue[256];
	private int top = 0;
	
	public AbstractMachineValue peek() {
		return registers[top];
	}
	
	public void push(AbstractMachineValue value) {
		if (top < registers.length) {
			registers[top++] = value;
		} else {
			throw new RuntimeException("Stack is full");
		}
	}
	
	public AbstractMachineValue pop() {
		if (top >= 0) {
			return registers[--top];
		} else {
			throw new RuntimeException("Stack is empty");
		}
	}
	
	public int size() {
		return top;
	}
	
	public String dump() {
		var sb = new StringBuilder();
		for(int i = 0 ; i < top; i++) {
			sb.append('[').append(i).append("] = ")
				.append(registers[i].toString()).append('\n');
		}
		return sb.toString();
	}
}