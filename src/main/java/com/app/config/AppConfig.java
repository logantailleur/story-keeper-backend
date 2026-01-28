package com.app.config;

import java.net.URI;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * Main application configuration.
 *
 * Database configuration (simple, no provider abstraction):
 * - Prefer `spring.datasource.url` if set (useful for tests/local overrides)
 * - Else use `DATABASE_URL` (Render/Heroku style:
 * postgres://user:pass@host:port/db)
 * - Else build PostgreSQL JDBC URL from `DB_*` environment variables
 */
@Configuration
public class AppConfig {

	private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

	@Bean
	@Primary
	public DataSource dataSource(Environment env) {
		String explicitUrl = env.getProperty("spring.datasource.url");
		if (StringUtils.hasText(explicitUrl)) {
			logger.info("Creating DataSource from spring.datasource.url");
			return DataSourceBuilder.create()
					.url(explicitUrl)
					.username(env.getProperty("spring.datasource.username", ""))
					.password(env.getProperty("spring.datasource.password", ""))
					.driverClassName(env.getProperty("spring.datasource.driver-class-name", ""))
					.build();
		}

		String databaseUrl = env.getProperty("DATABASE_URL");
		if (StringUtils.hasText(databaseUrl)) {
			logger.info("Creating DataSource from DATABASE_URL");
			return buildPostgresFromDatabaseUrl(databaseUrl);
		}

		// Fallback: build PostgreSQL config from individual DB_* variables
		String host = env.getProperty("DB_HOST", "localhost");
		String port = env.getProperty("DB_PORT", "5432");
		String dbName = env.getProperty("DB_NAME", "storykeeper");
		String username = env.getProperty("DB_USERNAME", "postgres");
		String password = env.getProperty("DB_PASSWORD", "");

		String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s", host, port, dbName);
		logger.info("Creating DataSource from DB_* variables (host={}, db={})", host, dbName);

		return DataSourceBuilder.create()
				.url(jdbcUrl)
				.username(username)
				.password(password)
				.driverClassName("org.postgresql.Driver")
				.build();
	}

	private DataSource buildPostgresFromDatabaseUrl(String databaseUrl) {
		// Accept:
		// - postgres://user:pass@host:port/db
		// - postgresql://user:pass@host:port/db
		// - jdbc:postgresql://host:port/db
		try {
			if (databaseUrl.startsWith("jdbc:")) {
				return DataSourceBuilder.create()
						.url(databaseUrl)
						.driverClassName("org.postgresql.Driver")
						.build();
			}

			String normalized = databaseUrl
					.replace("postgresql://", "http://")
					.replace("postgres://", "http://");

			URI uri = new URI(normalized);

			String username = "";
			String password = "";
			String userInfo = uri.getUserInfo();
			if (StringUtils.hasText(userInfo) && userInfo.contains(":")) {
				String[] credentials = userInfo.split(":", 2);
				username = credentials[0];
				password = credentials[1];
			}

			String host = uri.getHost();
			int port = uri.getPort() == -1 ? 5432 : uri.getPort();
			String path = uri.getPath();
			String db = (path != null && path.startsWith("/")) ? path.substring(1) : path;

			String jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s", host, port, db);

			return DataSourceBuilder.create()
					.url(jdbcUrl)
					.username(username)
					.password(password)
					.driverClassName("org.postgresql.Driver")
					.build();
		} catch (Exception e) {
			throw new IllegalStateException("Invalid DATABASE_URL: " + databaseUrl, e);
		}
	}
}
