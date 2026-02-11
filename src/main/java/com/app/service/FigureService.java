package com.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dto.figure.FigureCreateRequest;
import com.app.dto.figure.FigureResponse;
import com.app.dto.figure.FigureUpdateRequest;
import com.app.exception.CrossWorldLinkingException;
import com.app.exception.EventNotFoundException;
import com.app.exception.WorldNotFoundException;
import com.app.model.Event;
import com.app.model.Figure;
import com.app.model.FigureType;
import com.app.model.User;
import com.app.model.World;
import com.app.repository.EventRepository;
import com.app.repository.FigureRespository;
import com.app.repository.WorldRepository;

@Service
public class FigureService {

	@Autowired
	private FigureRespository figureRepository;

	@Autowired
	private WorldRepository worldRepository;

	@Autowired
	private EventRepository eventRepository;

	public FigureResponse createFigure(User currentUser, FigureCreateRequest request) {
		World world = getWorld(currentUser, request.getWorldId());
		Figure figure = new Figure();
		figure.setName(request.getName());
		figure.setType(FigureType.valueOf(request.getType().toUpperCase()));
		figure.setDescription(request.getDescription());
		figure.setWorld(world);
		return toResponse(figureRepository.save(figure));
	}

	public FigureResponse updateFigureById(User currentUser, Long id, FigureUpdateRequest request) {
		Figure figure = getFigure(id);
		getWorld(currentUser, figure.getWorld().getId());
		figure.setName(request.getName());
		figure.setType(FigureType.valueOf(request.getType().toUpperCase()));
		figure.setDescription(request.getDescription());
		return toResponse(figureRepository.save(figure));
	}

	public void deleteFigureById(User currentUser, Long id) {
		Figure figure = getFigure(id);
		getWorld(currentUser, figure.getWorld().getId());
		figureRepository.delete(figure);
	}

	public List<FigureResponse> getFiguresByWorldId(User currentUser, Long worldId) {
		World world = getWorld(currentUser, worldId);
		List<Figure> figures = figureRepository.findByWorldOrderByCreatedAtAsc(world);
		List<FigureResponse> figuresResponse = new ArrayList<>();
		for (Figure figure : figures) {
			figuresResponse.add(toResponse(figure));
		}
		return figuresResponse;
	}

	public FigureResponse getFigureById(User currentUser, Long id) {
		Figure figure = getFigure(id);
		getWorld(currentUser, figure.getWorld().getId());
		return toResponse(figure);
	}

	public FigureResponse linkEvent(User currentUser, Long figureId, Long eventId) {
		Figure figure = getFigure(figureId);
		getWorld(currentUser, figure.getWorld().getId());
		Event event = getEvent(eventId);
		getWorld(currentUser, event.getWorld().getId());

		validateWorldIntegrity(figure, event);

		figure.getEvents().add(event);
		return toResponse(figureRepository.save(figure));
	}

	public FigureResponse unlinkEvent(User currentUser, Long figureId, Long eventId) {
		Figure figure = getFigure(figureId);
		getWorld(currentUser, figure.getWorld().getId());
		Event event = getEvent(eventId);
		getWorld(currentUser, event.getWorld().getId());

		figure.getEvents().remove(event);
		return toResponse(figureRepository.save(figure));
	}

	private void validateWorldIntegrity(Figure figure, Event event) {
		if (!figure.getWorld().getId().equals(event.getWorld().getId())) {
			throw new CrossWorldLinkingException(
					"Cannot link figure and event from different worlds. Figure belongs to world " +
							figure.getWorld().getId() + " but event belongs to world " + event.getWorld().getId());
		}
	}

	private FigureResponse toResponse(Figure figure) {
		return new FigureResponse(figure.getId(), figure.getName(), figure.getType(), figure.getDescription(),
				figure.getWorld().getId(), figure.getCreatedAt(), figure.getUpdatedAt());
	}

	private World getWorld(User currentUser, Long worldId) {
		return worldRepository.findByIdAndUser(worldId, currentUser)
				.orElseThrow(() -> new WorldNotFoundException("World not found or access denied"));
	}

	private Figure getFigure(Long id) {
		return figureRepository.findById(id).orElseThrow(() -> new FigureNotFoundException(id));
	}

	private Event getEvent(Long id) {
		return eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
	}
}
