package com.app.dto.event;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EventCreateRequest {

	@NotBlank(message = "Title is required")
	@Size(max = 255, message = "Title must be less than 255 characters")
	private String title;

	@NotNull(message = "Year is required")
	@Min(value = 0, message = "Year must be greater than or equal to 0")
	private int year;

	@NotBlank(message = "Description is required")
	@Size(max = 1000, message = "Description must be less than 1000 characters")
	private String description;

	@NotNull(message = "World ID is required")
	private Long worldId;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getWorldId() {
		return worldId;
	}

	public void setWorldId(Long worldId) {
		this.worldId = worldId;
	}
}
