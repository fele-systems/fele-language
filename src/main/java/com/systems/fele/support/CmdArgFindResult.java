package com.systems.fele.support;

record CmdArgFindResult(CmdArgDefinition definition, int argIndex, String extractedValue) {
	boolean hasValue() {
		return extractedValue != null;
	}
}
