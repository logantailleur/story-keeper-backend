package com.app.dto;

/**
 * DTO for authentication response (register/login).
 */
public class AuthResponse {

	private String token;

	// Default constructor
	public AuthResponse() {
	}

	// Constructor with token
	public AuthResponse(String token) {
		this.token = token;
	}

	// Getters and setters
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
