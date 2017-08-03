package com.maven.web.util;

public enum UudevStatus {
	OK(1), 
	FAILED(2), 
	DATA_EMPTY(3), 
	WARNING(4),
	SYSTEM_ERROR(500);

	private final int value;

	private UudevStatus(int value) {
		this.value = value;
	}

	/**
	 * Return the integer value of this status code.
	 */
	public int value() {
		return this.value;
	}
}
