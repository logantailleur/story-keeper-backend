package com.storykeeper.config.database;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Factory for creating database providers.
 * Automatically discovers all available DatabaseProvider implementations
 * and allows selection based on configuration.
 */
@Component
public class DatabaseProviderFactory {
    
    private static final Logger logger = LoggerFactory.getLogger(DatabaseProviderFactory.class);
    
    private final Map<String, DatabaseProvider> providers;
    
    @Autowired
    public DatabaseProviderFactory(List<DatabaseProvider> providerList) {
        this.providers = providerList.stream()
            .collect(Collectors.toMap(
                DatabaseProvider::getProviderName,
                Function.identity()
            ));
        
        logger.info("Registered database providers: {}", providers.keySet());
    }
    
    /**
     * Get a database provider by name.
     * 
     * @param providerName the name of the provider (e.g., "postgresql", "h2", "mysql")
     * @return the DatabaseProvider instance
     * @throws IllegalArgumentException if the provider is not found
     */
    public DatabaseProvider getProvider(String providerName) {
        if (providerName == null || providerName.isEmpty()) {
            throw new IllegalArgumentException("Provider name cannot be null or empty");
        }
        
        DatabaseProvider provider = providers.get(providerName.toLowerCase());
        if (provider == null) {
            throw new IllegalArgumentException(
                String.format("Database provider '%s' not found. Available providers: %s", 
                            providerName, providers.keySet()));
        }
        
        logger.debug("Selected database provider: {}", providerName);
        return provider;
    }
    
    /**
     * Get all available provider names.
     * 
     * @return a list of provider names
     */
    public List<String> getAvailableProviders() {
        return List.copyOf(providers.keySet());
    }
}
