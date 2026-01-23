# Story Keeper Backend

A Spring Boot REST API application.

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── storykeeper/
│   │           ├── StoryKeeperApplication.java
│   │           ├── config/          # Configuration classes
│   │           ├── controller/       # REST controllers
│   │           ├── service/          # Business logic services
│   │           ├── repository/       # Data access layer
│   │           └── model/            # Entity models
│   └── resources/
│       └── application.properties
└── test/
    └── java/
        └── com/
            └── storykeeper/
```

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Running the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### H2 Console

Access the H2 database console at: `http://localhost:8080/h2-console`

## Technology Stack

- Spring Boot 3.2.0
- Spring Data JPA
- H2 Database (for development)
- Maven
# story-keeper-backend
