package com.storykeeper.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
public class AppConfig {

    @Value("${DATABASE_URL:}")
    private String databaseUrl;

    @Value("${DB_HOST:localhost}")
    private String dbHost;

    @Value("${DB_PORT:5432}")
    private String dbPort;

    @Value("${DB_NAME:storykeeper}")
    private String dbName;

    @Value("${DB_USERNAME:postgres}")
    private String dbUsername;

    @Value("${DB_PASSWORD:}")
    private String dbPassword;

    @Bean
    @Primary
    public DataSource dataSource() {
        DataSourceBuilder<?> builder = DataSourceBuilder.create();
        
        // If DATABASE_URL is provided (e.g., from Render), parse it
        if (databaseUrl != null && !databaseUrl.isEmpty()) {
            try {
                // Handle postgresql:// format from Render
                if (databaseUrl.startsWith("postgresql://") || databaseUrl.startsWith("postgres://")) {
                    URI dbUri = new URI(databaseUrl.replace("postgresql://", "http://").replace("postgres://", "http://"));
                    String username = dbUri.getUserInfo().split(":")[0];
                    String password = dbUri.getUserInfo().split(":")[1];
                    String host = dbUri.getHost();
                    int port = dbUri.getPort() == -1 ? 5432 : dbUri.getPort();
                    String path = dbUri.getPath().replaceFirst("/", "");
                    
                    builder.url("jdbc:postgresql://" + host + ":" + port + "/" + path);
                    builder.username(username);
                    builder.password(password);
                } else {
                    // Already in JDBC format
                    builder.url(databaseUrl);
                    builder.username(dbUsername);
                    builder.password(dbPassword);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse DATABASE_URL", e);
            }
        } else {
            // Use individual environment variables
            builder.url("jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName);
            builder.username(dbUsername);
            builder.password(dbPassword);
        }
        
        builder.driverClassName("org.postgresql.Driver");
        return builder.build();
    }
}
