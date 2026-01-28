# Story Keeper Backend

A Spring Boot REST API application.

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── app/
│   │           ├── Application.java
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
            └── app/
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

## Technology Stack

- Spring Boot 3.2.0
- Spring Data JPA
- H2 Database (test-only; optional)
- Maven

# story-keeper-backend
