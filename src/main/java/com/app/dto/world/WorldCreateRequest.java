package com.app.dto.world;

import com.app.model.User;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class WorldCreateRequest {

	@NotBlank(message = "Name is required")
	@Size(max = 255, message = "Name must be less than 255 characters")
	private String name;

	@Min(value = 0, message = "Start year must be greater than 0")
	private Integer startYear;

	@NotNull(message = "Current year is required")
	@Min(value = 0, message = "Current year must be greater than 0")
	private Integer currentYear;

	@NotBlank(message = "Description is required")
	@Size(max = 1000, message = "Description must be less than 1000 characters")
	private String description;

	@NotNull(message = "User is required")
	private User user;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStartYear() {
		return startYear;
	}

	public void setStartYear(Integer startYear) {
		this.startYear = startYear;
	}

	public Integer getCurrentYear() {
		return currentYear;
	}

	public void setCurrentYear(Integer currentYear) {
		this.currentYear = currentYear;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
