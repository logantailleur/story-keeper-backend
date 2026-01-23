package com.app.config.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Configuration provider that reads from environment variables.
 * This is the default implementation for most deployment scenarios.
 *
 * To use a different configuration provider (e.g., AWS Secrets Manager),
 * implement ConfigurationProvider and mark it as @Primary.
 */
@Component
@Primary
public class EnvironmentConfigurationProvider implements ConfigurationProvider {

    private static final Logger logger = LoggerFactory.getLogger(EnvironmentConfigurationProvider.class);

    private final Environment environment;

    public EnvironmentConfigurationProvider(Environment environment) {
        this.environment = environment;
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        String value = environment.getProperty(key, defaultValue);
        logger.debug("Config property '{}': {}", key, value != null && !value.equals(defaultValue) ? "***" : defaultValue);
        return value;
    }

    @Override
    public String getRequiredProperty(String key) {
        String value = environment.getProperty(key);
        if (value == null || value.isEmpty()) {
            throw new IllegalStateException("Required configuration property '" + key + "' is not set");
        }
        return value;
    }

    @Override
    public boolean hasProperty(String key) {
        return environment.containsProperty(key) &&
               environment.getProperty(key) != null &&
               !environment.getProperty(key).isEmpty();
    }
}
