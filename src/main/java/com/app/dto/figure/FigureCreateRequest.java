package com.app.dto.figure;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class FigureCreateRequest {

	@NotBlank(message = "Name is required")
	@Size(max = 255, message = "Name must be less than 255 characters")
	private String name;

	@NotBlank(message = "Type is required")
	@Size(max = 255, message = "Type must be less than 255 characters")
	private String type;

	@NotBlank(message = "Description is required")
	@Size(max = 1000, message = "Description must be less than 1000 characters")
	private String description;

	@NotNull(message = "World ID is required")
	private Long worldId;

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

	public Long getWorldId() {
		return worldId;
	}

	public void setWorldId(Long worldId) {
		this.worldId = worldId;
	}

}
