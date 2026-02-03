package com.app.exception;

public class WorldNotFoundException extends RuntimeException {

	public WorldNotFoundException(String message) {
		super(message);
	}

	public WorldNotFoundException(Long id) {
		super("World not found with id: " + id);
	}
}
