package com.storykeeper.config.database;

import java.net.URI;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;

/**
 * PostgreSQL database provider implementation.
 */
@Component
public class PostgreSQLProvider implements DatabaseProvider {
    
    private static final Logger logger = LoggerFactory.getLogger(PostgreSQLProvider.class);
    
    private static final String PROVIDER_NAME = "postgresql";
    private static final String DRIVER_CLASS = "org.postgresql.Driver";
    private static final String DIALECT = "org.hibernate.dialect.PostgreSQLDialect";
    private static final int DEFAULT_PORT = 5432;
    
    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }
    
    @Override
    public DataSource createDataSource(DatabaseConfig config) {
        logger.info("Creating PostgreSQL DataSource for host: {}, database: {}", 
                   config.getHost(), config.getDatabaseName());
        
        DataSourceBuilder<?> builder = DataSourceBuilder.create();
        builder.url(config.getJdbcUrl());
        builder.username(config.getUsername());
        builder.password(config.getPassword());
        builder.driverClassName(config.getDriverClassName());
        
        return builder.build();
    }
    
    @Override
    public String getHibernateDialect() {
        return DIALECT;
    }
    
    @Override
    public DatabaseConfig parseDatabaseUrl(String databaseUrl) {
        if (databaseUrl == null || databaseUrl.isEmpty()) {
            throw new IllegalArgumentException("Database URL cannot be null or empty");
        }
        
        DatabaseConfig config = new DatabaseConfig();
        config.setDriverClassName(DRIVER_CLASS);
        
        try {
            // Handle postgresql:// or postgres:// format
            if (databaseUrl.startsWith("postgresql://") || databaseUrl.startsWith("postgres://")) {
                URI dbUri = new URI(
                    databaseUrl.replace("postgresql://", "http://")
                              .replace("postgres://", "http://"));
                
                String userInfo = dbUri.getUserInfo();
                if (userInfo != null && userInfo.contains(":")) {
                    String[] credentials = userInfo.split(":", 2);
                    config.setUsername(credentials[0]);
                    config.setPassword(credentials[1]);
                }
                
                config.setHost(dbUri.getHost());
                config.setPort(String.valueOf(dbUri.getPort() == -1 ? DEFAULT_PORT : dbUri.getPort()));
                
                String path = dbUri.getPath();
                if (path != null && path.startsWith("/")) {
                    config.setDatabaseName(path.substring(1));
                } else {
                    config.setDatabaseName(path);
                }
            } else {
                // Already in JDBC format: jdbc:postgresql://host:port/database
                // Extract components from JDBC URL
                String jdbcPrefix = "jdbc:postgresql://";
                if (databaseUrl.startsWith(jdbcPrefix)) {
                    String connectionPart = databaseUrl.substring(jdbcPrefix.length());
                    String[] parts = connectionPart.split("/", 2);
                    
                    if (parts.length == 2) {
                        String[] hostPort = parts[0].split(":");
                        config.setHost(hostPort[0]);
                        config.setPort(hostPort.length > 1 ? hostPort[1] : String.valueOf(DEFAULT_PORT));
                        config.setDatabaseName(parts[1]);
                    }
                } else {
                    throw new IllegalArgumentException("Unsupported database URL format: " + databaseUrl);
                }
            }
            
            config.setJdbcUrl(buildJdbcUrl(config));
            logger.debug("Parsed PostgreSQL URL - Host: {}, Port: {}, Database: {}", 
                        config.getHost(), config.getPort(), config.getDatabaseName());
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse PostgreSQL DATABASE_URL: " + databaseUrl, e);
        }
        
        return config;
    }
    
    @Override
    public String buildJdbcUrl(DatabaseConfig config) {
        if (config.getJdbcUrl() != null && !config.getJdbcUrl().isEmpty()) {
            return config.getJdbcUrl();
        }
        
        return String.format("jdbc:postgresql://%s:%s/%s",
                           config.getHost(),
                           config.getPort(),
                           config.getDatabaseName());
    }
}
