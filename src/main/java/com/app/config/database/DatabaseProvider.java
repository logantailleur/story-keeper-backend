package com.app.config.database;

import javax.sql.DataSource;

/**
 * Abstraction for database providers.
 * Allows switching between different database systems:
 * - PostgreSQL
 * - MySQL
 * - H2 (for development)
 * - SQL Server
 * - etc.
 */
public interface DatabaseProvider {

    /**
     * Get the name of this database provider (e.g., "postgresql", "mysql", "h2").
     *
     * @return the provider name
     */
    String getProviderName();

    /**
     * Create a DataSource from the provided database configuration.
     *
     * @param config the database configuration
     * @return a configured DataSource
     */
    DataSource createDataSource(DatabaseConfig config);

    /**
     * Get the Hibernate dialect class name for this database provider.
     *
     * @return the dialect class name
     */
    String getHibernateDialect();

    /**
     * Parse a DATABASE_URL connection string into a DatabaseConfig.
     *
     * @param databaseUrl the connection string
     * @return a DatabaseConfig object
     */
    DatabaseConfig parseDatabaseUrl(String databaseUrl);

    /**
     * Build a JDBC URL from individual configuration components.
     *
     * @param config the database configuration
     * @return the JDBC URL string
     */
    String buildJdbcUrl(DatabaseConfig config);
}
