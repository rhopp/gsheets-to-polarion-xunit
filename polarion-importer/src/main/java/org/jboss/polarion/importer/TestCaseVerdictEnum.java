package org.jboss.polarion.importer;

import java.util.Arrays;

public enum TestCaseVerdictEnum {

	PASSED("Passed"), FAILED("Failed"), BLOCKED("Blocked");

	private final String text;

	private TestCaseVerdictEnum(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}

	public static TestCaseVerdictEnum lookup(String verdict) {
		return Arrays.stream(values())
				.filter(e -> e.name().equalsIgnoreCase(verdict))
				.findFirst()
				.orElse(null);
	}

}
