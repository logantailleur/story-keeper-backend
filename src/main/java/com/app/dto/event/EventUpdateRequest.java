package com.app.dto.event;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class EventUpdateRequest {

	@Size(max = 255, message = "Title must be less than 255 characters")
	private String title;

	@Min(value = 0, message = "Year must be greater than or equal to 0")
	private Integer year;

	@Size(max = 1000, message = "Description must be less than 1000 characters")
	private String description;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
