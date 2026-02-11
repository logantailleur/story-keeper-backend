package com.app.dto.figure;

import jakarta.validation.constraints.Size;

public class FigureUpdateRequest {

	@Size(max = 255, message = "Name must be less than 255 characters")
	private String name;

	@Size(max = 255, message = "Type must be less than 255 characters")
	private String type;

	@Size(max = 1000, message = "Description must be less than 1000 characters")
	private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
