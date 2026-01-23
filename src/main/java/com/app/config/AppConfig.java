package com.app.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.app.config.database.DatabaseConfig;
import com.app.config.database.DatabaseConfigurationService;
import com.app.config.database.DatabaseProvider;
import com.app.config.database.DatabaseProviderFactory;

/**
 * Main application configuration.
 * Now uses modular abstraction layers for database and configuration providers.
 *
 * To switch database providers, set DB_PROVIDER environment variable:
 * - postgresql (default)
 * - h2 (for local development)
 * - mysql (if MySQLProvider is implemented)
 *
 * To switch configuration providers, implement ConfigurationProvider interface
 * and configure it as the primary bean.
 */
@Configuration
public class AppConfig {

	private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

	private final DatabaseConfigurationService databaseConfigService;
	private final DatabaseProviderFactory providerFactory;

	@Autowired
	public AppConfig(DatabaseConfigurationService databaseConfigService,
	                 DatabaseProviderFactory providerFactory) {
		this.databaseConfigService = databaseConfigService;
		this.providerFactory = providerFactory;
	}

	@Bean
	@Primary
	public DataSource dataSource() {
		logger.info("Initializing DataSource using modular configuration system");

		// Load database configuration from the centralized service
		DatabaseConfig config = databaseConfigService.loadDatabaseConfig();

		// Get the provider name that was used to load the config
		String providerName = databaseConfigService.getProviderName();
		DatabaseProvider provider = providerFactory.getProvider(providerName);

		// Create DataSource using the provider
		DataSource dataSource = provider.createDataSource(config);

		logger.info("DataSource created successfully using provider: {}", providerName);
		return dataSource;
	}
}
