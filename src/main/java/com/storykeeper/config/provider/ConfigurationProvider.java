package com.storykeeper.config.provider;

/**
 * Abstraction for configuration providers.
 * Allows switching between different configuration sources:
 * - Environment variables
 * - AWS Secrets Manager
 * - Azure Key Vault
 * - HashiCorp Vault
 * - etc.
 */
public interface ConfigurationProvider {
    
    /**
     * Get a configuration property value.
     * 
     * @param key the property key
     * @param defaultValue the default value if key is not found
     * @return the property value or defaultValue
     */
    String getProperty(String key, String defaultValue);
    
    /**
     * Get a required configuration property value.
     * 
     * @param key the property key
     * @return the property value
     * @throws IllegalStateException if the property is not found
     */
    String getRequiredProperty(String key);
    
    /**
     * Check if a property exists.
     * 
     * @param key the property key
     * @return true if the property exists
     */
    boolean hasProperty(String key);
}
