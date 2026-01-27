package com.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.model.User;
import com.app.repository.UserRepository;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtService jwtService;

	public String registerUser(String email, String password) {
		if (userRepository.existsByEmail(email)) {
			throw new RuntimeException("User already exists");
		}

		User user = new User();
		user.setEmail(email);
		user.setPasswordHash(passwordEncoder.encode(password));
		userRepository.save(user);

		return jwtService.generateToken(user.getId(), email);
	}

	public String loginUser(String email, String password) {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			throw new RuntimeException("User not found");
		}

		if (!passwordEncoder.matches(password, user.getPasswordHash())) {
			throw new RuntimeException("Invalid password");
		}

		return jwtService.generateToken(user.getId(), email);
	}

	public String getEmailFromToken(String token) {
		if (token == null || !token.startsWith("Bearer ")) {
			throw new RuntimeException("Invalid token");
		}

		String jwtToken = token.substring(7);
		if (!jwtService.validateToken(jwtToken)) {
			throw new RuntimeException("Invalid token");
		}

		return jwtService.extractEmail(jwtToken);
	}

	public User getUserByEmail(String email) {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			throw new RuntimeException("User not found");
		}
		return user;
	}
}
