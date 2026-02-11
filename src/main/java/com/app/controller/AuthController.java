package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.auth.AuthResponse;
import com.app.dto.auth.LoginRequest;
import com.app.dto.auth.RegisterRequest;
import com.app.dto.user.UserResponse;
import com.app.model.User;
import com.app.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthController {

	@Autowired
	private AuthService authService;

	@GetMapping("/me")
	public ResponseEntity<?> me(@AuthenticationPrincipal UserDetails userDetails) {
		try {
			// Get email from UserDetails (username is email in our implementation)
			String email = userDetails.getUsername();
			User user = authService.getUserByEmail(email);
			UserResponse response = new UserResponse(user.getId(), user.getEmail(), user.getCreatedAt());
			return ResponseEntity.ok(response);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve user");
		}
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
		try {
			String token = authService.registerUser(request.getEmail(), request.getPassword());
			return ResponseEntity.ok(new AuthResponse(token));
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed");
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
		try {
			String token = authService.loginUser(request.getEmail(), request.getPassword());
			return ResponseEntity.ok(new AuthResponse(token));
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed");
		}
	}
}
