package com.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "events")
public class Event extends BaseEntity {

	@Column(name = "title", nullable = false, length = 255)
	private String title;

	@Column(name = "year", nullable = false, precision = 4)
	private int year;

	@Column(name = "description", length = 1000)
	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "world_id", nullable = false)
	@NotNull(message = "World is required")
	private World world;

	public Event(String title, int year, String description, World world) {
		this.title = title;
		this.year = year;
		this.description = description;
		this.world = world;
	}

	public Event() {
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

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	@AssertTrue(message = "Year must be in the world's timeline range")
	public boolean isYearInWorldTimelineRange() {
		return year >= world.getStartYear() && year <= world.getCurrentYear();
	}
}
