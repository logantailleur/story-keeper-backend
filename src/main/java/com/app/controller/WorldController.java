package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.world.WorldCreateRequest;
import com.app.dto.world.WorldResponse;
import com.app.dto.world.WorldUpdateRequest;
import com.app.model.User;
import com.app.service.AuthService;
import com.app.service.WorldService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/worlds")
public class WorldController {

	@Autowired
	private WorldService worldService;

	@Autowired
	private AuthService authService;

	@GetMapping
	public List<WorldResponse> getUserWorlds(@AuthenticationPrincipal UserDetails userDetails) {
		User currentUser = authService.getUserByEmail(userDetails.getUsername());
		return worldService.getUserWorlds(currentUser);
	}

	@PostMapping
	public WorldResponse createWorld(
			@AuthenticationPrincipal UserDetails userDetails,
			@Valid @RequestBody WorldCreateRequest request) {
		User currentUser = authService.getUserByEmail(userDetails.getUsername());
		return worldService.createWorld(currentUser, request);
	}

	@GetMapping("/{id}")
	public WorldResponse getWorldById(
			@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Long id) {
		User currentUser = authService.getUserByEmail(userDetails.getUsername());
		return worldService.getWorldById(currentUser, id);
	}

	@PatchMapping("/{id}")
	public WorldResponse updateWorld(
			@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Long id,
			@Valid @RequestBody WorldUpdateRequest request) {
		User currentUser = authService.getUserByEmail(userDetails.getUsername());
		return worldService.updateWorld(currentUser, id, request);
	}

	@DeleteMapping("/{id}")
	public void deleteWorld(
			@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Long id) {
		User currentUser = authService.getUserByEmail(userDetails.getUsername());
		worldService.deleteWorld(currentUser, id);
	}
}
