package com.app.config;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.app.config.database.DatabaseConfigurationService;

/**
 * JPA/Hibernate configuration that dynamically sets the dialect
 * based on the configured database provider.
 */
@Configuration
public class JpaConfig {

    private final DatabaseConfigurationService databaseConfigService;

    @Autowired
    public JpaConfig(DatabaseConfigurationService databaseConfigService) {
        this.databaseConfigService = databaseConfigService;
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return hibernateProperties -> {
            // Dynamically set the Hibernate dialect based on the database provider
            String dialect = databaseConfigService.getHibernateDialect();
            hibernateProperties.put(AvailableSettings.DIALECT, dialect);
        };
    }
}
