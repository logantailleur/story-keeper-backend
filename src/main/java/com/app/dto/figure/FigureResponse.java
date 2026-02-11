package com.app.dto.figure;

import java.time.LocalDateTime;
import java.util.List;

import com.app.model.FigureType;

public class FigureResponse {

	private Long id;

	private String name;

	private FigureType type;

	private String description;

	private Long worldId;

	private List<Long> eventIds;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	public FigureResponse(Long id, String name, FigureType type, String description, Long worldId,
			List<Long> eventIds, LocalDateTime createdAt,
			LocalDateTime updatedAt) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.description = description;
		this.worldId = worldId;
		this.eventIds = eventIds;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FigureType getType() {
		return type;
	}

	public void setType(FigureType type) {
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<Long> getEventIds() {
		return eventIds;
	}

	public void setEventIds(List<Long> eventIds) {
		this.eventIds = eventIds;
	}
}
