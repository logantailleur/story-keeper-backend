package com.storykeeper.config.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.storykeeper.config.provider.ConfigurationProvider;

/**
 * Service responsible for loading and preparing database configuration.
 * Centralizes all database configuration logic in one place.
 */
@Service
public class DatabaseConfigurationService {
    
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfigurationService.class);
    
    private final ConfigurationProvider configProvider;
    private final DatabaseProviderFactory providerFactory;
    
    @Autowired
    public DatabaseConfigurationService(ConfigurationProvider configProvider,
                                       DatabaseProviderFactory providerFactory) {
        this.configProvider = configProvider;
        this.providerFactory = providerFactory;
    }
    
    /**
     * Load database configuration from the configuration provider.
     * 
     * @return a configured DatabaseConfig object
     */
    public DatabaseConfig loadDatabaseConfig() {
        // Determine which database provider to use
        String providerName = configProvider.getProperty("DB_PROVIDER", "postgresql");
        logger.info("Using database provider: {}", providerName);
        
        DatabaseProvider provider = providerFactory.getProvider(providerName);
        
        // Check if DATABASE_URL is provided (common in cloud platforms)
        String databaseUrl = configProvider.getProperty("DATABASE_URL", "");
        
        DatabaseConfig config;
        if (databaseUrl != null && !databaseUrl.isEmpty()) {
            logger.info("Using DATABASE_URL for database connection");
            config = provider.parseDatabaseUrl(databaseUrl);
        } else {
            logger.info("Using individual database configuration properties");
            config = loadFromIndividualProperties(provider);
        }
        
        // Ensure driver class name is set
        if (config.getDriverClassName() == null || config.getDriverClassName().isEmpty()) {
            // This shouldn't happen if provider is implemented correctly, but safety check
            throw new IllegalStateException("Driver class name not set for provider: " + providerName);
        }
        
        logger.info("Database configuration loaded - Host: {}, Database: {}, Provider: {}", 
                   config.getHost(), config.getDatabaseName(), providerName);
        
        return config;
    }
    
    /**
     * Load database configuration from individual environment variables.
     */
    private DatabaseConfig loadFromIndividualProperties(DatabaseProvider provider) {
        DatabaseConfig config = new DatabaseConfig();
        
        // Set provider-specific defaults
        String defaultHost = "localhost";
        String defaultPort = "5432";
        String defaultDatabase = "storykeeper";
        String defaultUsername = "postgres";
        String defaultPassword = "";
        
        // Override defaults based on provider
        if (provider.getProviderName().equals("h2")) {
            defaultPort = "0";
            defaultUsername = "sa";
        }
        
        config.setHost(configProvider.getProperty("DB_HOST", defaultHost));
        config.setPort(configProvider.getProperty("DB_PORT", defaultPort));
        config.setDatabaseName(configProvider.getProperty("DB_NAME", defaultDatabase));
        config.setUsername(configProvider.getProperty("DB_USERNAME", defaultUsername));
        config.setPassword(configProvider.getProperty("DB_PASSWORD", defaultPassword));
        
        // Get driver class name from provider-specific logic
        // For now, we'll set it based on provider, but providers should ideally provide this
        if (provider.getProviderName().equals("h2")) {
            config.setDriverClassName("org.h2.Driver");
        } else if (provider.getProviderName().equals("postgresql")) {
            config.setDriverClassName("org.postgresql.Driver");
        } else {
            // For future providers, they should provide a method to get driver class name
            throw new IllegalStateException("Driver class name not determined for provider: " + provider.getProviderName());
        }
        
        // Build JDBC URL using provider
        config.setJdbcUrl(provider.buildJdbcUrl(config));
        
        return config;
    }
    
    /**
     * Get the current database provider name.
     * 
     * @return the provider name (e.g., "postgresql", "h2")
     */
    public String getProviderName() {
        return configProvider.getProperty("DB_PROVIDER", "postgresql");
    }
    
    /**
     * Get the Hibernate dialect for the configured database provider.
     * 
     * @return the Hibernate dialect class name
     */
    public String getHibernateDialect() {
        String providerName = getProviderName();
        DatabaseProvider provider = providerFactory.getProvider(providerName);
        return provider.getHibernateDialect();
    }
}
