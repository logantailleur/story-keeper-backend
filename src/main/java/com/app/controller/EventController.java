package com.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.event.EventCreateRequest;
import com.app.dto.event.EventResponse;
import com.app.dto.event.EventUpdateRequest;
import com.app.model.User;
import com.app.service.AuthService;
import com.app.service.EventService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/events")
public class EventController extends BaseController {

	@Autowired
	private EventService eventService;

	@Autowired
	private AuthService authService;

	@GetMapping
	public List<EventResponse> getWorldsEvents(
			@RequestParam Long worldId,
			@AuthenticationPrincipal UserDetails userDetails) {
		User currentUser = authService.getUserByEmail(userDetails.getUsername());
		return eventService.getEventsByWorldId(currentUser, worldId);
	}

	@PostMapping
	public EventResponse createEvent(
			@AuthenticationPrincipal UserDetails userDetails,
			@Valid @RequestBody EventCreateRequest request) {
		User currentUser = authService.getUserByEmail(userDetails.getUsername());
		return eventService.createEvent(currentUser, request);
	}

	@GetMapping("/{id}")
	public EventResponse getEventById(
			@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
		User currentUser = authService.getUserByEmail(userDetails.getUsername());
		return eventService.getEventByIdAndWorldId(currentUser, id);
	}

	@PatchMapping("/{id}")
	public EventResponse updateEvent(
			@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Long id,
			@Valid @RequestBody EventUpdateRequest request) {
		User currentUser = authService.getUserByEmail(userDetails.getUsername());
		return eventService.updateEventById(currentUser, id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, String>> deleteEvent(
			@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Long id) {
		User currentUser = authService.getUserByEmail(userDetails.getUsername());
		eventService.deleteEvent(currentUser, id);
		return deleteSuccessResponse("Event");
	}
}
