package com.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dto.event.EventCreateRequest;
import com.app.dto.event.EventResponse;
import com.app.dto.event.EventUpdateRequest;
import com.app.exception.CrossWorldLinkingException;
import com.app.exception.EventNotFoundException;
import com.app.exception.InvalidYearException;
import com.app.exception.WorldNotFoundException;
import com.app.model.Event;
import com.app.model.Figure;
import com.app.model.User;
import com.app.model.World;
import com.app.repository.EventRepository;
import com.app.repository.FigureRespository;
import com.app.repository.WorldRepository;

@Service
public class EventService {

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private WorldRepository worldRepository;

	@Autowired
	private FigureRespository figureRepository;

	public EventResponse createEvent(User currentUser, EventCreateRequest request) {
		World world = getWorld(currentUser, request.getWorldId());
		validateYearInWorldBounds(request.getYear(), world);
		Event event = new Event();
		event.setTitle(request.getTitle());
		event.setYear(request.getYear());
		event.setDescription(request.getDescription());
		event.setWorld(world);
		return toResponse(eventRepository.save(event));
	}

	public List<EventResponse> getEventsByWorldId(User currentUser, Long worldId) {
		World world = getWorld(currentUser, worldId);
		List<Event> events = eventRepository.findByWorldOrderByYearAsc(world);

		List<EventResponse> eventsResponse = new ArrayList<>();
		for (Event event : events) {
			eventsResponse.add(toResponse(event));
		}
		return eventsResponse;
	}

	public EventResponse getEventByIdAndWorldId(User currentUser, Long id) {
		Event event = getEvent(id);
		getWorld(currentUser, event.getWorld().getId());
		return toResponse(event);
	}

	public EventResponse updateEventById(User currentUser, Long id, EventUpdateRequest request) {
		Event event = getEvent(id);
		World world = getWorld(currentUser, event.getWorld().getId());

		if (request.getTitle() != null) {
			event.setTitle(request.getTitle());
		}
		if (request.getDescription() != null) {
			event.setDescription(request.getDescription());
		}

		Integer reqYear = request.getYear();
		if (reqYear != null) {
			validateYearInWorldBounds(reqYear, world);
			event.setYear(reqYear);
		}

		return toResponse(eventRepository.save(event));
	}

	public void deleteEvent(User currentUser, Long id) {
		Event event = getEvent(id);
		getWorld(currentUser, event.getWorld().getId());
		eventRepository.delete(event);
	}

	public EventResponse linkFigure(User currentUser, Long eventId, Long figureId) {
		Event event = getEvent(eventId);
		getWorld(currentUser, event.getWorld().getId());
		Figure figure = getFigure(figureId);
		getWorld(currentUser, figure.getWorld().getId());

		validateWorldIntegrity(figure, event);

		// Manipulate from owning side (Figure) for proper JPA persistence
		figure.getEvents().add(event);
		figureRepository.save(figure);
		
		// Return refreshed event
		return toResponse(eventRepository.findById(eventId).orElse(event));
	}

	public EventResponse unlinkFigure(User currentUser, Long eventId, Long figureId) {
		Event event = getEvent(eventId);
		getWorld(currentUser, event.getWorld().getId());
		Figure figure = getFigure(figureId);
		getWorld(currentUser, figure.getWorld().getId());

		// Manipulate from owning side (Figure) for proper JPA persistence
		figure.getEvents().remove(event);
		figureRepository.save(figure);
		
		// Return refreshed event
		return toResponse(eventRepository.findById(eventId).orElse(event));
	}

	private void validateWorldIntegrity(Figure figure, Event event) {
		if (!figure.getWorld().getId().equals(event.getWorld().getId())) {
			throw new CrossWorldLinkingException(
					"Cannot link figure and event from different worlds. Figure belongs to world " +
							figure.getWorld().getId() + " but event belongs to world " + event.getWorld().getId());
		}
	}

	private Figure getFigure(Long id) {
		return figureRepository.findById(id)
				.orElseThrow(() -> new com.app.service.FigureNotFoundException(id));
	}

	private EventResponse toResponse(Event event) {
		return new EventResponse(event.getId(), event.getTitle(), event.getYear(), event.getDescription(),
				event.getWorld().getId(), event.getCreatedAt(), event.getUpdatedAt());
	}

	private World getWorld(User currentUser, Long worldId) {
		return worldRepository.findByIdAndUser(worldId, currentUser)
				.orElseThrow(() -> new WorldNotFoundException("World not found or access denied"));
	}

	private Event getEvent(Long id) {
		return eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
	}

	private void validateYearInWorldBounds(int year, World world) {
		if (year < world.getStartYear() || year > world.getCurrentYear()) {
			throw new InvalidYearException(
					"Year must be between " + world.getStartYear() + " and " + world.getCurrentYear());
		}
	}
}
