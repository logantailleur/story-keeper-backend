package com.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * Test class for JwtService.
 * 
 * To run this test:
 * 1. Set JWT_SECRET environment variable or use test property
 * 2. Run: mvn test -Dtest=JwtServiceTest
 * 
 * Example:
 * export JWT_SECRET=your-256-bit-secret-key-here
 * mvn test -Dtest=JwtServiceTest
 */
@SpringBootTest
@TestPropertySource(properties = {
		"JWT_SECRET=test-secret-key-for-jwt-service-testing-must-be-at-least-32-characters-long-for-hmac-sha256",
		"spring.datasource.url=jdbc:h2:mem:testdb",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.jpa.hibernate.ddl-auto=create-drop",
		"spring.jpa.database-platform="
})
class JwtServiceTest {

	@Autowired
	private JwtService jwtService;

	@Test
	void testGenerateToken() {
		Long userId = 1L;
		String email = "test@example.com";

		String token = jwtService.generateToken(userId, email);

		assertNotNull(token);
		assertTrue(token.length() > 0);
	}

	@Test
	void testValidateToken() {
		Long userId = 1L;
		String email = "test@example.com";

		String token = jwtService.generateToken(userId, email);

		assertTrue(jwtService.validateToken(token));
	}

	@Test
	void testValidateInvalidToken() {
		String invalidToken = "invalid.token.here";

		assertTrue(!jwtService.validateToken(invalidToken));
	}

	@Test
	void testExtractUserId() {
		Long userId = 123L;
		String email = "user@example.com";

		String token = jwtService.generateToken(userId, email);

		Long extractedUserId = jwtService.extractUserId(token);

		assertEquals(userId, extractedUserId);
	}

	@Test
	void testExtractEmail() {
		Long userId = 1L;
		String email = "test@example.com";

		String token = jwtService.generateToken(userId, email);

		String extractedEmail = jwtService.extractEmail(token);

		assertEquals(email, extractedEmail);
	}

	@Test
	void testGenerateAndParseToken() {
		Long userId = 42L;
		String email = "integration@test.com";

		// Generate token
		String token = jwtService.generateToken(userId, email);
		assertNotNull(token);

		// Validate token
		assertTrue(jwtService.validateToken(token));

		// Extract and verify user ID
		Long extractedUserId = jwtService.extractUserId(token);
		assertEquals(userId, extractedUserId);

		// Extract and verify email
		String extractedEmail = jwtService.extractEmail(token);
		assertEquals(email, extractedEmail);
	}
}
