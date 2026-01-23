# Provider Configuration Guide

This guide explains how to switch between different database and configuration providers in the Story Keeper Backend.

## Table of Contents

- [Database Providers](#database-providers)
- [Configuration Providers](#configuration-providers)
- [Quick Reference](#quick-reference)
- [Examples](#examples)
- [Adding New Providers](#adding-new-providers)

---

## Database Providers

The application supports multiple database providers. Switch between them using the `DB_PROVIDER` environment variable.

### Available Database Providers

#### PostgreSQL (Default)
- **Provider Name**: `postgresql`
- **Use Case**: Production and development
- **Driver**: `org.postgresql.Driver`
- **Dialect**: `org.hibernate.dialect.PostgreSQLDialect`

#### H2
- **Provider Name**: `h2`
- **Use Case**: Local development and testing
- **Driver**: `org.h2.Driver`
- **Dialect**: `org.hibernate.dialect.H2Dialect`

### Switching Database Providers

#### Method 1: Environment Variable

```bash
# Use PostgreSQL (default)
export DB_PROVIDER=postgresql

# Use H2 for local development
export DB_PROVIDER=h2
```

#### Method 2: Application Properties

Add to `application.properties` or `application-dev.yml`:

```properties
# In application.properties
DB_PROVIDER=h2
```

```yaml
# In application-dev.yml
DB_PROVIDER: h2
```

#### Method 3: Docker/Render Environment Variables

```yaml
# In render.yaml or docker-compose.yml
envVars:
  - key: DB_PROVIDER
    value: postgresql
```

### Database Connection Configuration

Database connections can be configured in two ways:

#### Option 1: DATABASE_URL (Recommended for Cloud Platforms)

```bash
# PostgreSQL format
export DATABASE_URL=postgresql://username:password@host:port/database

# Example
export DATABASE_URL=postgresql://postgres:mypassword@localhost:5432/storykeeper
```

#### Option 2: Individual Environment Variables

```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=storykeeper
export DB_USERNAME=postgres
export DB_PASSWORD=mypassword
```

**Note**: If both `DATABASE_URL` and individual variables are set, `DATABASE_URL` takes precedence.

---

## Configuration Providers

Configuration providers determine where the application reads its configuration from (environment variables, secrets managers, etc.).

### Default: Environment Variables

The default `EnvironmentConfigurationProvider` reads from:
- System environment variables
- Application properties files
- Spring Boot's `Environment` abstraction

No configuration needed - this is the default behavior.

### Switching Configuration Providers

To use a different configuration source (e.g., AWS Secrets Manager, Azure Key Vault):

1. **Implement the `ConfigurationProvider` interface**:

```java
package com.storykeeper.config.provider;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary  // This makes it the active configuration provider
public class AwsSecretsManagerProvider implements ConfigurationProvider {
    
    @Override
    public String getProperty(String key, String defaultValue) {
        // Fetch from AWS Secrets Manager
        // Return defaultValue if not found
    }
    
    @Override
    public String getRequiredProperty(String key) {
        // Fetch from AWS Secrets Manager
        // Throw exception if not found
    }
    
    @Override
    public boolean hasProperty(String key) {
        // Check if property exists in AWS Secrets Manager
    }
}
```

2. **Mark your implementation as `@Primary`** to override the default provider

3. **That's it!** The rest of the application will automatically use your new configuration provider.

---

## Quick Reference

### Environment Variables

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| `DB_PROVIDER` | Database provider name (`postgresql`, `h2`) | `postgresql` | No |
| `DATABASE_URL` | Full database connection string | - | No* |
| `DB_HOST` | Database host | `localhost` | No* |
| `DB_PORT` | Database port | `5432` | No* |
| `DB_NAME` | Database name | `storykeeper` | No* |
| `DB_USERNAME` | Database username | `postgres` | No* |
| `DB_PASSWORD` | Database password | - | No* |

\* Either `DATABASE_URL` or all individual `DB_*` variables must be provided.

### Common Scenarios

#### Local Development with H2

```bash
export DB_PROVIDER=h2
# No other DB variables needed - H2 uses in-memory database
```

#### Local Development with PostgreSQL

```bash
export DB_PROVIDER=postgresql
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=storykeeper
export DB_USERNAME=postgres
export DB_PASSWORD=yourpassword
```

#### Production (Render/Heroku)

```bash
# Render automatically sets DATABASE_URL
export DB_PROVIDER=postgresql
# DATABASE_URL is automatically parsed
```

#### Docker Compose

```yaml
services:
  app:
    environment:
      - DB_PROVIDER=postgresql
      - DB_HOST=postgres
      - DB_PORT=5432
      - DB_NAME=storykeeper
      - DB_USERNAME=postgres
      - DB_PASSWORD=postgres
```

---

## Examples

### Example 1: Switch to H2 for Testing

**Goal**: Use H2 in-memory database for quick local testing.

**Steps**:
1. Set environment variable:
   ```bash
   export DB_PROVIDER=h2
   ```
2. Run the application:
   ```bash
   mvn spring-boot:run
   ```

**Result**: Application uses H2 database. No PostgreSQL connection needed.

---

### Example 2: Use PostgreSQL with Custom Connection

**Goal**: Connect to a remote PostgreSQL database.

**Steps**:
1. Set environment variables:
   ```bash
   export DB_PROVIDER=postgresql
   export DB_HOST=my-remote-server.com
   export DB_PORT=5432
   export DB_NAME=mydatabase
   export DB_USERNAME=myuser
   export DB_PASSWORD=mypassword
   ```
2. Run the application.

**Result**: Application connects to the remote PostgreSQL database.

---

### Example 3: Use DATABASE_URL from Cloud Platform

**Goal**: Deploy to Render/Heroku which provides `DATABASE_URL`.

**Steps**:
1. Set only:
   ```bash
   export DB_PROVIDER=postgresql
   # DATABASE_URL is automatically set by the platform
   ```
2. Deploy.

**Result**: Application automatically parses `DATABASE_URL` and connects.

---

### Example 4: Implement AWS Secrets Manager Provider

**Goal**: Use AWS Secrets Manager instead of environment variables.

**Steps**:

1. Add AWS SDK dependency to `pom.xml`:
   ```xml
   <dependency>
       <groupId>software.amazon.awssdk</groupId>
       <artifactId>secretsmanager</artifactId>
   </dependency>
   ```

2. Create `AwsSecretsManagerProvider.java`:
   ```java
   package com.storykeeper.config.provider;
   
   import org.springframework.context.annotation.Primary;
   import org.springframework.stereotype.Component;
   import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
   import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
   import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
   
   @Component
   @Primary
   public class AwsSecretsManagerProvider implements ConfigurationProvider {
       
       private final SecretsManagerClient secretsManager;
       
       public AwsSecretsManagerProvider() {
           this.secretsManager = SecretsManagerClient.create();
       }
       
       @Override
       public String getProperty(String key, String defaultValue) {
           try {
               GetSecretValueRequest request = GetSecretValueRequest.builder()
                   .secretId(key)
                   .build();
               GetSecretValueResponse response = secretsManager.getSecretValue(request);
               return response.secretString();
           } catch (Exception e) {
               return defaultValue;
           }
       }
       
       @Override
       public String getRequiredProperty(String key) {
           String value = getProperty(key, null);
           if (value == null) {
               throw new IllegalStateException("Required secret '" + key + "' not found");
           }
           return value;
       }
       
       @Override
       public boolean hasProperty(String key) {
           try {
               return getProperty(key, null) != null;
           } catch (Exception e) {
               return false;
           }
       }
   }
   ```

3. Store secrets in AWS Secrets Manager with keys like:
   - `DB_HOST`
   - `DB_PASSWORD`
   - etc.

4. Run the application - it will automatically use AWS Secrets Manager.

---

## Adding New Providers

### Adding a New Database Provider

1. **Create a new class implementing `DatabaseProvider`**:

```java
package com.storykeeper.config.database;

import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;

@Component
public class MySQLProvider implements DatabaseProvider {
    
    private static final String PROVIDER_NAME = "mysql";
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
    private static final String DIALECT = "org.hibernate.dialect.MySQLDialect";
    private static final int DEFAULT_PORT = 3306;
    
    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }
    
    @Override
    public DataSource createDataSource(DatabaseConfig config) {
        DataSourceBuilder<?> builder = DataSourceBuilder.create();
        builder.url(config.getJdbcUrl());
        builder.username(config.getUsername());
        builder.password(config.getPassword());
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
        // Parse mysql://user:password@host:port/database format
        // Implementation similar to PostgreSQLProvider
        return config;
    }
    
    @Override
    public String buildJdbcUrl(DatabaseConfig config) {
        return String.format("jdbc:mysql://%s:%s/%s",
                           config.getHost(),
                           config.getPort(),
                           config.getDatabaseName());
    }
}
```

2. **Add the database driver dependency to `pom.xml`**:

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

3. **Use it**:

```bash
export DB_PROVIDER=mysql
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=storykeeper
export DB_USERNAME=root
export DB_PASSWORD=password
```

The `DatabaseProviderFactory` will automatically discover and register your new provider!

### Adding a New Configuration Provider

1. **Implement `ConfigurationProvider` interface**
2. **Mark as `@Component` and `@Primary`**
3. **That's it!** The application will use it automatically.

See [Example 4](#example-4-implement-aws-secrets-manager-provider) above for a complete example.

---

## Troubleshooting

### Issue: Provider not found

**Error**: `Database provider 'xyz' not found`

**Solution**: 
- Check that `DB_PROVIDER` value matches exactly (case-insensitive): `postgresql`, `h2`, etc.
- Verify the provider class is annotated with `@Component`
- Check application logs for registered providers

### Issue: Database connection fails

**Error**: Connection refused or authentication failed

**Solution**:
- Verify `DATABASE_URL` format or individual `DB_*` variables
- Check database is running and accessible
- Verify credentials are correct
- Check firewall/network settings

### Issue: Hibernate dialect error

**Error**: Wrong dialect or dialect not found

**Solution**:
- Ensure `DB_PROVIDER` is set correctly
- The dialect is automatically set based on provider
- Check `JpaConfig` is loading correctly

### Issue: Configuration provider not working

**Error**: Configuration values not loading from custom provider

**Solution**:
- Ensure your provider is marked with `@Primary`
- Verify `@Component` annotation is present
- Check that your provider is in the component scan path
- Review application startup logs for provider registration

---

## Architecture Notes

### How It Works

1. **Application Startup**:
   - `DatabaseProviderFactory` discovers all `DatabaseProvider` implementations
   - `DatabaseConfigurationService` loads configuration using `ConfigurationProvider`
   - `AppConfig` creates `DataSource` using the selected provider
   - `JpaConfig` sets Hibernate dialect dynamically

2. **Configuration Flow**:
   ```
   ConfigurationProvider → DatabaseConfigurationService → DatabaseProvider → DataSource
   ```

3. **Provider Discovery**:
   - All `@Component` annotated providers are automatically discovered
   - No manual registration needed
   - Factory pattern manages provider selection

### Key Benefits

- ✅ **Single Point of Change**: All database config in `DatabaseConfigurationService`
- ✅ **Easy Switching**: Just change `DB_PROVIDER` environment variable
- ✅ **Extensible**: Add new providers by implementing interfaces
- ✅ **Backward Compatible**: Existing configurations still work
- ✅ **Type Safe**: Interfaces ensure correct implementation

---

## Additional Resources

- See `ProviderExamples.java` for code templates
- Check `DatabaseConfigurationService` for configuration logic
- Review `AppConfig` for DataSource creation
- Examine `JpaConfig` for Hibernate configuration
