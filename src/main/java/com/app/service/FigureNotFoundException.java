package com.app.service;

public class FigureNotFoundException extends RuntimeException {

	public FigureNotFoundException(Long id) {
		super("Figure not found with id: " + id);
	}

	public FigureNotFoundException(String message) {
		super(message);
	}
}
