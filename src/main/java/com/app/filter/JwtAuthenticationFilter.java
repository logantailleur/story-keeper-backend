package com.app.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.app.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JWT Authentication Filter
 * 
 * Responsibilities:
 * - Reads Authorization: Bearer <token> header
 * - Validates JWT token
 * - Loads user from database
 * - Attaches user to Spring Security context
 * 
 * This enables the use of @AuthenticationPrincipal in controllers.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BEARER_PREFIX = "Bearer ";

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(@org.springframework.lang.NonNull HttpServletRequest request,
			@org.springframework.lang.NonNull HttpServletResponse response,
			@org.springframework.lang.NonNull FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader(AUTHORIZATION_HEADER);

		// If no Authorization header or doesn't start with "Bearer ", continue without authentication
		if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
			filterChain.doFilter(request, response);
			return;
		}

		// Extract token (remove "Bearer " prefix)
		String token = authHeader.substring(BEARER_PREFIX.length());

		// Validate token
		if (!jwtService.validateToken(token)) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			// Extract email from token
			String email = jwtService.extractEmail(token);

			// Load user details
			UserDetails userDetails = userDetailsService.loadUserByUsername(email);

			// Create authentication token
			Authentication authentication = new UsernamePasswordAuthenticationToken(
					userDetails,
					null,
					userDetails.getAuthorities());

			// Set authentication in SecurityContext
			SecurityContextHolder.getContext().setAuthentication(authentication);

		} catch (Exception e) {
			// If anything goes wrong (invalid token, user not found, etc.),
			// continue without authentication
			// The request will be handled as unauthenticated
		}

		// Continue filter chain
		filterChain.doFilter(request, response);
	}
}
