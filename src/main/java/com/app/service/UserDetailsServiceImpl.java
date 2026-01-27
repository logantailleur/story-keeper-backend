package com.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.repository.UserRepository;

/**
 * UserDetailsService implementation for Spring Security.
 * 
 * Loads user from database by email and converts to Spring Security UserDetails.
 * This is used by JwtAuthenticationFilter to load user information.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		com.app.model.User appUser = userRepository.findByEmail(email);

		if (appUser == null) {
			throw new UsernameNotFoundException("User not found with email: " + email);
		}

		// Return Spring Security UserDetails
		// Note: We don't need password for JWT authentication, but Spring Security requires it
		// We use an empty string since we're using JWT tokens, not password-based auth
		return User.builder()
				.username(appUser.getEmail())
				.password("") // Not used in JWT authentication
				.authorities("ROLE_USER") // Default role
				.build();
	}
}
