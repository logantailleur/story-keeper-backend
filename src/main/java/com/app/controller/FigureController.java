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

import com.app.dto.figure.FigureCreateRequest;
import com.app.dto.figure.FigureResponse;
import com.app.dto.figure.FigureUpdateRequest;
import com.app.model.User;
import com.app.service.AuthService;
import com.app.service.FigureService;

@RestController
@RequestMapping("/api/figures")
public class FigureController extends BaseController {

	@Autowired
	private FigureService figureService;

	@Autowired
	private AuthService authService;

	@GetMapping
	public List<FigureResponse> getFiguresByWorldId(
			@RequestParam Long worldId,
			@AuthenticationPrincipal UserDetails userDetails) {
		User currentUser = authService.getUserByEmail(userDetails.getUsername());
		return figureService.getFiguresByWorldId(currentUser, worldId);
	}

	@GetMapping("/{id}")
	public FigureResponse getFigureById(@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Long id) {
		User currentUser = authService.getUserByEmail(userDetails.getUsername());
		return figureService.getFigureById(currentUser, id);
	}

	@PostMapping
	public FigureResponse createFigure(@AuthenticationPrincipal UserDetails userDetails,
			@RequestBody FigureCreateRequest request) {
		User currentUser = authService.getUserByEmail(userDetails.getUsername());
		return figureService.createFigure(currentUser, request);
	}

	@PatchMapping("/{id}")
	public FigureResponse updateFigureById(@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Long id,
			@RequestBody FigureUpdateRequest request) {
		User currentUser = authService.getUserByEmail(userDetails.getUsername());
		return figureService.updateFigureById(currentUser, id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, String>> deleteFigureById(@AuthenticationPrincipal UserDetails userDetails,
			@PathVariable Long id) {
		User currentUser = authService.getUserByEmail(userDetails.getUsername());
		figureService.deleteFigureById(currentUser, id);
		return deleteSuccessResponse("Figure");
	}
}
