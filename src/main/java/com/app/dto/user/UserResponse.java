package com.app.dto.user;

import java.time.LocalDateTime;

/**
 * DTO for user information response.
 */
public class UserResponse {

	private Long id;
	private String email;
	private LocalDateTime createdAt;

	// Default constructor
	public UserResponse() {
	}

	// Constructor with all fields
	public UserResponse(Long id, String email, LocalDateTime createdAt) {
		this.id = id;
		this.email = email;
		this.createdAt = createdAt;
	}

	// Getters and setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
