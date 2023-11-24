package com.systems.fele.support;

import java.util.List;
import java.util.stream.Collectors;

public record CmdArg(int aliasIndex, CmdArgDefinition definition, List<String> extractedValues) {
	public boolean hasValue() {
		return !extractedValues.isEmpty();
	}
	
	public int count() {
		return extractedValues.size();
	}
	
	public boolean isOn() {
		return count() > 0;
	}
	
	public int[] getNumbers() {
		return extractedValues.stream()
				.mapToInt(this::parseNumber)
				.toArray();
	}
	
	public List<String> getStrings() {
		return extractedValues.stream()
			.map(this::parseString)
			.collect(Collectors.toList());
	}
	
	public int getNumber(int i) {
		return parseNumber(extractedValues.get(i));
	}
	
	public String getString(int i) {
		return parseString(extractedValues.get(i));
	}
	
	private int parseNumber(String extractedValue) {
		if (extractedValue == null || extractedValue.isBlank()) {
			if (definition.defaultValue == null) {
				throw new RuntimeException("The argument %s has no value!".formatted(definition.aliases[aliasIndex]));
			} else {
				return Integer.parseInt(definition.defaultValue.strip());
			}
		} else {
			return Integer.parseInt(extractedValue.strip());
		}
	}
	
	private String parseString(String extractedValue) {
		if (extractedValue == null) {
			if (definition.defaultValue == null) {
				throw new RuntimeException("The argument %s has no value!".formatted(definition.aliases[aliasIndex]));
			} else {
				return definition.defaultValue.strip();
			}
		} else {
			return extractedValue.strip();
		}
	}
	
	
	
}
