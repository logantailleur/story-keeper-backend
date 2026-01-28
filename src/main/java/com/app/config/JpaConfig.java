package com.app.config;

import org.springframework.context.annotation.Configuration;

/**
 * JPA/Hibernate configuration.
 * 
 * Note: Hibernate 6+ automatically detects the dialect from the DataSource,
 * so explicit dialect configuration is no longer needed.
 */
@Configuration
public class JpaConfig {
    // Hibernate dialect is now auto-detected from the DataSource
    // No explicit configuration needed
}
