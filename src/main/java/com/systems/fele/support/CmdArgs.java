package com.systems.fele.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

public class CmdArgs {
	
	List<CmdArgDefinition> definitions;
	Map<String, CmdArg> values;
	
	public CmdArgs() {
		definitions = new ArrayList<>();
		values = new HashMap<>();
	}
	
	public CmdArgs define(UnaryOperator<CmdArgDefinition> definer) {
		definitions.add( definer.apply( new CmdArgDefinition() ) );
		return this;
	}
	
	public void parse(String[] args) {
		for (String arg : args) arg = arg.strip();
		
		for (var i = 0; i < args.length; i++) {
			if (args[i].charAt(0) != '-') {
				var result = findArg("--");
				if (result == null)
					throw new RuntimeException("Argument not recognized %s".formatted(args[i]));
				var value = values.get("--");
				if (value == null) {
					value = new CmdArg(result.argIndex(), result.definition(), new ArrayList<>());
					values.put("--", value);
				}
				value.extractedValues().add(args[i]);
				continue;
			}
			
			var result = findArg(args[i]);
			if (result == null) {
				 throw new RuntimeException("Could not find argument %s".formatted(args[i]));
			}
			
			var value = values.get(result.definition().aliases[result.argIndex()]);
			if (value != null) {
				if (!result.definition().list) throw new RuntimeException("Argument %s cannot be duplicated.".formatted(result.definition().aliases[result.argIndex()]));
				value.extractedValues().add(result.extractedValue());
			} else {
				var extractedValues = new ArrayList<String>(1);
				extractedValues.add(result.extractedValue());
				var arg = new CmdArg(result.argIndex(),
								   	 result.definition(),
								   	 extractedValues);
				for (var alias : result.definition().aliases) values.put(alias, arg);
			}
			
		}
	}
	
	public CmdArg arg(String name) {
		var a = values.get(name);
		if (a == null) {
			var r = findArg(name);
			if (r == null) {
				throw new RuntimeException("Could not find argument %s".formatted(name));
			} else {
				return new CmdArg(r.argIndex(), r.definition(), Arrays.asList());
			}
		} else {
			return a;
		}
		 
	}
	
	private CmdArgFindResult findArg(String arg) {		
		for (var definition : definitions) {
			var result = argMatches(arg, definition);
			if (result != null) return result;
		}
		return null;
	}

	private CmdArgFindResult argMatches(String arg, CmdArgDefinition definition) {
		for (var i = 0; i < definition.aliases.length; i++) {
			final var alias = definition.aliases[i];
			if (arg.startsWith(alias)) {
				final String extractedValue;
				if (alias.charAt(alias.length()-1) == '=') {
					extractedValue = arg.substring(alias.length());
				} else if (alias.equals(arg)) {
					extractedValue = "";
				} else {
					return null;					
				}
				return new CmdArgFindResult(definition, i, extractedValue);
			}
		}
		return null;
	}
}
