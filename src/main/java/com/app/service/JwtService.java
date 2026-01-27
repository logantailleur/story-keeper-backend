package com.app.service;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * JWT utility service for token generation, validation, and parsing.
 * 
 * Uses JWT_SECRET environment variable for signing tokens.
 * Tokens expire after 7 days.
 */
@Service
public class JwtService {

	private static final long EXPIRATION_TIME_MS = 7L * 24 * 60 * 60 * 1000; // 7 days in milliseconds
	private static final String EMAIL_CLAIM = "email";

	@Value("${JWT_SECRET:}")
	private String jwtSecret;

	/**
	 * Generates a JWT token for a user.
	 * 
	 * @param userId The user's ID (stored in 'sub' claim)
	 * @param email  The user's email (stored in 'email' claim)
	 * @return JWT token string
	 * @throws IllegalStateException if JWT_SECRET is not configured
	 */
	public String generateToken(Long userId, String email) {
		if (jwtSecret == null || jwtSecret.isEmpty()) {
			throw new IllegalStateException("JWT_SECRET environment variable is not set");
		}

		SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
		Date now = new Date();
		Date expiration = new Date(now.getTime() + EXPIRATION_TIME_MS);

		return Jwts.builder()
				.subject(userId.toString())
				.claim(EMAIL_CLAIM, email)
				.issuedAt(now)
				.expiration(expiration)
				.signWith(key)
				.compact();
	}

	/**
	 * Validates a JWT token.
	 * 
	 * @param token The JWT token to validate
	 * @return true if token is valid, false otherwise
	 */
	public boolean validateToken(String token) {
		try {
			if (jwtSecret == null || jwtSecret.isEmpty()) {
				return false;
			}

			SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
			Jwts.parser()
					.verifyWith(key)
					.build()
					.parseSignedClaims(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	/**
	 * Extracts the user ID from a JWT token.
	 * 
	 * @param token The JWT token
	 * @return User ID as Long
	 * @throws JwtException          if token is invalid or cannot be parsed
	 * @throws IllegalStateException if JWT_SECRET is not configured
	 */
	public Long extractUserId(String token) {
		if (jwtSecret == null || jwtSecret.isEmpty()) {
			throw new IllegalStateException("JWT_SECRET environment variable is not set");
		}

		SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
		Claims claims = Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();

		String subject = claims.getSubject();
		if (subject == null) {
			throw new JwtException("Token does not contain a subject (user ID)");
		}

		try {
			return Long.parseLong(subject);
		} catch (NumberFormatException e) {
			throw new JwtException("Token subject is not a valid user ID: " + subject);
		}
	}

	/**
	 * Extracts the email from a JWT token.
	 * 
	 * @param token The JWT token
	 * @return Email address
	 * @throws JwtException          if token is invalid or cannot be parsed
	 * @throws IllegalStateException if JWT_SECRET is not configured
	 */
	public String extractEmail(String token) {
		if (jwtSecret == null || jwtSecret.isEmpty()) {
			throw new IllegalStateException("JWT_SECRET environment variable is not set");
		}

		SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
		Claims claims = Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();

		return claims.get(EMAIL_CLAIM, String.class);
	}
}
