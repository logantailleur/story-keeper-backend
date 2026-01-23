package com.storykeeper.config.database;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;

/**
 * H2 database provider implementation.
 * Useful for local development and testing.
 */
@Component
public class H2Provider implements DatabaseProvider {
    
    private static final Logger logger = LoggerFactory.getLogger(H2Provider.class);
    
    private static final String PROVIDER_NAME = "h2";
    private static final String DRIVER_CLASS = "org.h2.Driver";
    private static final String DIALECT = "org.hibernate.dialect.H2Dialect";
    
    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }
    
    @Override
    public DataSource createDataSource(DatabaseConfig config) {
        logger.info("Creating H2 DataSource for in-memory development database");
        
        DataSourceBuilder<?> builder = DataSourceBuilder.create();
        builder.url(config.getJdbcUrl() != null ? config.getJdbcUrl() : 
                   "jdbc:h2:mem:storykeeper;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        builder.username(config.getUsername() != null ? config.getUsername() : "sa");
        builder.password(config.getPassword() != null ? config.getPassword() : "");
        builder.driverClassName(DRIVER_CLASS);
        
        return builder.build();
    }
    
    @Override
    public String getHibernateDialect() {
        return DIALECT;
    }
    
    @Override
    public DatabaseConfig parseDatabaseUrl(String databaseUrl) {
        DatabaseConfig config = new DatabaseConfig();
        config.setDriverClassName(DRIVER_CLASS);
        
        if (databaseUrl != null && !databaseUrl.isEmpty()) {
            config.setJdbcUrl(databaseUrl);
        } else {
            config.setJdbcUrl("jdbc:h2:mem:storykeeper;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        }
        
        config.setUsername("sa");
        config.setPassword("");
        config.setHost("localhost");
        config.setPort("0");
        config.setDatabaseName("storykeeper");
        
        return config;
    }
    
    @Override
    public String buildJdbcUrl(DatabaseConfig config) {
        if (config.getJdbcUrl() != null && !config.getJdbcUrl().isEmpty()) {
            return config.getJdbcUrl();
        }
        
        return "jdbc:h2:mem:storykeeper;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE";
    }
}
