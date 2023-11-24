package com.systems.fele.support;

public class CmdArgDefinition {
	
	String[] aliases;
	CmdArgType type;
	boolean positional = false;
	boolean list = false;
	String defaultValue;
	
	public CmdArgDefinition names(String name, String... aliases) {
		this.aliases = new String[aliases.length+1];
		this.aliases[0] = name;
		for(var i = 0; i < aliases.length; i++) {
			this.aliases[i+1] = aliases[i];
		}
		
		return this;
	}	
	
	public CmdArgDefinition setDefault(String defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}
	
	public CmdArgDefinition setList() {
		list = true;
		return this;
	}
	
	public CmdArgDefinition asPositional() {
		positional = true;
		return this;
	}
	
	public CmdArgDefinition asString() {
		type = CmdArgType.STRING;
		return this;
	}
	
	public CmdArgDefinition asNumber() {
		type = CmdArgType.NUMBER;
		return this;
	}
	
	public CmdArgDefinition asSwitch() {
		type = CmdArgType.SWITCH;
		return this;
	}
}
