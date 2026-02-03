package com.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "worlds")
public class World extends BaseEntity {
	@Column(name = "name", nullable = false, length = 255)
	@NotBlank(message = "Name is required")
	@Size(max = 255, message = "Name must be less than 255 characters")
	private String name;

	@Column(name = "start_year", nullable = false, precision = 4)
	private int startYear;

	@Column(name = "current_year", precision = 4)
	private int currentYear;

	@Column(name = "description", length = 1000)
	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	@NotNull(message = "User is required")
	private User user;

	public World(String name, int startYear, int currentYear, String description, User user) {
		this.name = name;
		this.startYear = startYear;
		this.currentYear = currentYear;
		this.description = description;
		this.user = user;
	}

	public World(String name, int currentYear, String description, User user) {
		this.name = name;
		this.currentYear = currentYear;
		this.description = description;
		this.user = user;
		this.startYear = 0;
	}

	public World() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStartYear() {
		return startYear;
	}

	public void setStartYear(int startYear) {
		this.startYear = startYear;
	}

	public int getCurrentYear() {
		return currentYear;
	}

	public void setCurrentYear(int currentYear) {
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

	@AssertTrue(message = "Start year must be before current year")
	public boolean isStartYearBeforeCurrentYear() {
		return startYear <= currentYear;
	}

	@AssertTrue(message = "Current year must be greater than 0")
	public boolean isCurrentYearGreaterThan0() {
		return currentYear > 0;
	}

	@AssertTrue(message = "Start year must be greater than or equal to 0")
	public boolean isStartYearGreaterThanOrEqualTo0() {
		return startYear >= 0;
	}
}
