package com.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security-related bean configuration.
 * Provides password encoding beans for authentication.
 */
@Configuration
public class SecurityBeansConfig {

	/**
	 * BCrypt password encoder bean for hashing passwords.
	 * Used in user registration and login authentication.
	 * 
	 * @return BCryptPasswordEncoder instance
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
