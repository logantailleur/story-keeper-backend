package com.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "figures")
public class Figure extends BaseEntity {

	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@Column(name = "type", nullable = false)
	private FigureType type;

	@Column(name = "description", length = 1000)
	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "world_id", nullable = false)
	@NotNull(message = "World is required")
	private World world;

	public Figure(String name, FigureType type, String description, World world) {
		this.name = name;
		this.type = type;
		this.description = description;
		this.world = world;
	}

	public Figure() {
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

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

}
