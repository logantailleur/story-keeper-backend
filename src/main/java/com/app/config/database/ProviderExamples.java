package com.app.config.database;

/**
 * Example: MySQL Database Provider Implementation
 *
 * This is a template showing how to add a new database provider.
 * To use MySQL:
 *
 * 1. Add MySQL dependency to pom.xml:
 *    <dependency>
 *        <groupId>com.mysql</groupId>
 *        <artifactId>mysql-connector-j</artifactId>
 *        <scope>runtime</scope>
 *    </dependency>
 *
 * 2. Uncomment and implement this class
 * 3. Set DB_PROVIDER=mysql in your environment
 *
 * Example implementation:
 *
 * @Component
 * public class MySQLProvider implements DatabaseProvider {
 *
 *     private static final String PROVIDER_NAME = "mysql";
 *     private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
 *     private static final String DIALECT = "org.hibernate.dialect.MySQLDialect";
 *     private static final int DEFAULT_PORT = 3306;
 *
 *     @Override
 *     public String getProviderName() {
 *         return PROVIDER_NAME;
 *     }
 *
 *     @Override
 *     public DataSource createDataSource(DatabaseConfig config) {
 *         DataSourceBuilder<?> builder = DataSourceBuilder.create();
 *         builder.url(config.getJdbcUrl());
 *         builder.username(config.getUsername());
 *         builder.password(config.getPassword());
 *         builder.driverClassName(DRIVER_CLASS);
 *         return builder.build();
 *     }
 *
 *     @Override
 *     public String getHibernateDialect() {
 *         return DIALECT;
 *     }
 *
 *     @Override
 *     public DatabaseConfig parseDatabaseUrl(String databaseUrl) {
 *         // Parse mysql://user:password@host:port/database format
 *         // Similar to PostgreSQLProvider implementation
 *         DatabaseConfig config = new DatabaseConfig();
 *         config.setDriverClassName(DRIVER_CLASS);
 *         // ... parsing logic ...
 *         return config;
 *     }
 *
 *     @Override
 *     public String buildJdbcUrl(DatabaseConfig config) {
 *         return String.format("jdbc:mysql://%s:%s/%s",
 *                            config.getHost(),
 *                            config.getPort(),
 *                            config.getDatabaseName());
 *     }
 * }
 *
 *
 *
 * Example: AWS Secrets Manager Configuration Provider
 *
 * To use AWS Secrets Manager instead of environment variables:
 *
 * 1. Add AWS SDK dependency to pom.xml:
 *    <dependency>
 *        <groupId>software.amazon.awssdk</groupId>
 *        <artifactId>secretsmanager</artifactId>
 *    </dependency>
 *
 * 2. Implement ConfigurationProvider:
 *
 * @Component
 * @Primary  // Mark as primary to override EnvironmentConfigurationProvider
 * public class AwsSecretsManagerProvider implements ConfigurationProvider {
 *
 *     private final SecretsManagerClient secretsManager;
 *
 *     public AwsSecretsManagerProvider() {
 *         this.secretsManager = SecretsManagerClient.create();
 *     }
 *
 *     @Override
 *     public String getProperty(String key, String defaultValue) {
 *         try {
 *             GetSecretValueRequest request = GetSecretValueRequest.builder()
 *                 .secretId(key)
 *                 .build();
 *             GetSecretValueResponse response = secretsManager.getSecretValue(request);
 *             return response.secretString();
 *         } catch (Exception e) {
 *             return defaultValue;
 *         }
 *     }
 *
 *     // ... implement other methods ...
 * }
 *
 * 3. That's it! The rest of your application will automatically use AWS Secrets Manager.
 */

public class ProviderExamples {
    // This file is for documentation purposes only
    // It shows examples of how to extend the modular system
}
