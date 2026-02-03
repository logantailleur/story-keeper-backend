package com.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dto.world.WorldCreateRequest;
import com.app.dto.world.WorldResponse;
import com.app.dto.world.WorldUpdateRequest;
import com.app.exception.WorldNotFoundException;
import com.app.model.User;
import com.app.model.World;
import com.app.repository.WorldRepository;

@Service
public class WorldService {
	@Autowired
	private WorldRepository worldRepository;

	public WorldResponse createWorld(User currentUser, WorldCreateRequest request) {
		World world = new World();
		world.setName(request.getName());
		world.setStartYear(request.getStartYear());
		world.setCurrentYear(request.getCurrentYear());
		world.setDescription(request.getDescription());
		world.setUser(currentUser);

		World savedWorld = worldRepository.save(world);
		return toResponse(savedWorld);
	}

	public WorldResponse getWorldById(User currentUser, Long id) {
		World world = worldRepository.findByIdAndUser(id, currentUser)
				.orElseThrow(() -> new WorldNotFoundException(id));
		return toResponse(world);
	}

	public List<WorldResponse> getUserWorlds(User currentUser) {
		List<World> worlds = worldRepository.findByUser(currentUser);
		List<WorldResponse> responses = new ArrayList<>();
		for (World world : worlds) {
			responses.add(toResponse(world));
		}

		return responses;
	}

	public WorldResponse updateWorld(User currentUser, Long id, WorldUpdateRequest request) {
		World world = worldRepository.findByIdAndUser(id, currentUser)
				.orElseThrow(() -> new WorldNotFoundException(id));

		if (request.getName() != null) {
			world.setName(request.getName());
		}
		if (request.getStartYear() != null) {
			world.setStartYear(request.getStartYear());
		}
		if (request.getCurrentYear() != null) {
			world.setCurrentYear(request.getCurrentYear());
		}
		if (request.getDescription() != null) {
			world.setDescription(request.getDescription());
		}

		worldRepository.save(world);
		return toResponse(world);
	}

	public void deleteWorld(User currentUser, Long id) {
		World world = worldRepository.findByIdAndUser(id, currentUser)
				.orElseThrow(() -> new WorldNotFoundException(id));
		worldRepository.delete(world);
	}

	private WorldResponse toResponse(World world) {
		WorldResponse response = new WorldResponse();
		response.setId(world.getId());
		response.setName(world.getName());
		response.setStartYear(world.getStartYear());
		response.setCurrentYear(world.getCurrentYear());
		response.setDescription(world.getDescription());
		response.setCreatedAt(world.getCreatedAt());
		response.setUpdatedAt(world.getUpdatedAt());
		return response;
	}
}
