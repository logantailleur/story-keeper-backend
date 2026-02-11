package com.app.dto.event;

import java.time.LocalDateTime;

public class EventResponse {

	private Long id;

	private String title;

	private int year;

	private String description;

	private Long worldId;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	public EventResponse(Long id, String title, int year, String description, Long worldId, LocalDateTime createdAt,
			LocalDateTime updatedAt) {
		this.id = id;
		this.title = title;
		this.year = year;
		this.description = description;
		this.worldId = worldId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
}
