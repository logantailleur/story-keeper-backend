package com.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Configuration
public class CorsConfig {

	@Value("${cors.allowed.origins:}")
	private String allowedOrigins;

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(@NonNull CorsRegistry registry) {
				// Build list of origin patterns
				List<String> originPatterns = buildOriginPatterns();

				registry.addMapping("/**") // apply to all endpoints
						// Use allowedOriginPatterns for wildcard support (Spring 5.3+)
						// This allows Vercel preview deployments (*.vercel.app)
						.allowedOriginPatterns(originPatterns.toArray(new String[0]))
						.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
						.allowedHeaders("*")
						.allowCredentials(true)
						.maxAge(3600); // Cache preflight requests for 1 hour
			}
		};
	}

	/**
	 * Build list of allowed origin patterns
	 * Supports:
	 * - Local development (localhost with common ports)
	 * - Vercel production and preview deployments (*.vercel.app pattern)
	 * - Custom origins from environment variable (comma-separated)
	 */
	private List<String> buildOriginPatterns() {
		List<String> patterns = new ArrayList<>();

		// Add localhost patterns for development
		patterns.add("http://localhost:*");
		patterns.add("http://127.0.0.1:*");

		// Add Vercel patterns (supports production and preview deployments)
		// Production: https://story-keeper-frontend.vercel.app
		// Previews: https://story-keeper-frontend-*.vercel.app
		patterns.add("https://*.vercel.app");

		// Parse and add custom origins from environment variable
		if (allowedOrigins != null && !allowedOrigins.trim().isEmpty()) {
			Stream.of(allowedOrigins.split(","))
					.map(String::trim)
					.filter(s -> !s.isEmpty())
					.forEach(patterns::add);
		}

		return patterns;
	}
}
