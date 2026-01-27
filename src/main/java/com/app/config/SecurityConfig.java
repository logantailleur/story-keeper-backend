package com.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.app.filter.JwtAuthenticationFilter;

/**
 * Spring Security Configuration
 * 
 * Rules:
 * - Disable CSRF (API-only)
 * - Enable CORS
 * - Permit: /api/login, /api/register
 * - Secure: everything else under /api/**
 * 
 * JWT filter is added before UsernamePasswordAuthenticationFilter
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			// Disable CSRF for API-only application
			.csrf(csrf -> csrf.disable())
			// Enable CORS (uses CorsConfig bean)
			.cors(cors -> {})
			// Stateless session management (JWT-based)
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			// Authorization rules
			.authorizeHttpRequests(auth -> auth
				// Public endpoints
				.requestMatchers("/api/login", "/api/register").permitAll()
				// Secure everything else under /api/**
				.requestMatchers("/api/**").authenticated()
				// All other requests (non-API) are permitted
				.anyRequest().permitAll())
			// Add JWT filter before UsernamePasswordAuthenticationFilter
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
