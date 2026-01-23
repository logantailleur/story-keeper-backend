# Guide to Adding New Endpoints

This guide explains how to add new REST API endpoints to the Story Keeper Backend application. Follow these steps to ensure consistency with the existing codebase architecture.

## Project Architecture

The application follows a layered architecture:

```
Controller Layer (REST API)
    ↓
Service Layer (Business Logic) - Optional but recommended
    ↓
Repository Layer (Data Access)
    ↓
Model Layer (Entities)
```

## Step-by-Step Guide

### 1. Create the Entity Model

All entities should extend `BaseEntity`, which provides:
- `id` (Long, auto-generated)
- `createdAt` (LocalDateTime, auto-set on creation)
- `updatedAt` (LocalDateTime, auto-updated on modification)

**Example: Creating a new entity**

```java
package com.storykeeper.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "your_table_name")
public class YourEntity extends BaseEntity {

    @Column(name = "field_name", nullable = false, length = 255)
    private String fieldName;

    // Default constructor (required by JPA)
    public YourEntity() {
    }

    // Getters and setters
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
```

**Key Points:**
- Use `@Entity` annotation
- Use `@Table` to specify the database table name
- Use `@Column` for field-level configuration (nullable, length, unique, etc.)
- Always include a default constructor
- Provide getters and setters for all fields

### 2. Create the Repository Interface

Repositories extend `BaseRepository`, which provides standard JPA methods (`findAll`, `findById`, `save`, `delete`, etc.).

**Example: Creating a repository**

```java
package com.storykeeper.repository;

import org.springframework.stereotype.Repository;
import com.storykeeper.model.YourEntity;

@Repository
public interface YourEntityRepository extends BaseRepository<YourEntity, Long> {
    // Add custom query methods here if needed
    // Example: List<YourEntity> findByFieldName(String fieldName);
}
```

**Key Points:**
- Extend `BaseRepository<YourEntity, Long>` (Long is the ID type)
- Use `@Repository` annotation
- Add custom query methods using Spring Data JPA naming conventions

### 3. Create the Service Layer (Optional but Recommended)

While not strictly required, services encapsulate business logic and make controllers cleaner.

**Example: Creating a service**

```java
package com.storykeeper.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.storykeeper.model.YourEntity;
import com.storykeeper.repository.YourEntityRepository;

@Service
public class YourEntityService {
    
    @Autowired
    private YourEntityRepository repository;
    
    public List<YourEntity> getAllEntities() {
        return repository.findAll();
    }
    
    public YourEntity getEntityById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Entity not found with id: " + id));
    }
    
    public YourEntity createEntity(YourEntity entity) {
        return repository.save(entity);
    }
    
    public YourEntity updateEntity(Long id, YourEntity entity) {
        YourEntity existing = getEntityById(id);
        // Update fields
        existing.setFieldName(entity.getFieldName());
        return repository.save(existing);
    }
    
    public void deleteEntity(Long id) {
        repository.deleteById(id);
    }
}
```

**Key Points:**
- Use `@Service` annotation
- Inject repository using `@Autowired`
- Handle business logic and validation here
- Throw appropriate exceptions for error cases

### 4. Create the Controller

Controllers handle HTTP requests and responses. They should be thin and delegate to services.

**Example: Creating a controller**

```java
package com.storykeeper.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.storykeeper.model.YourEntity;
import com.storykeeper.service.YourEntityService;

@RestController
@RequestMapping("/api/your-entities")
public class YourEntityController {

    @Autowired
    private YourEntityService service;

    @GetMapping
    public ResponseEntity<List<YourEntity>> getAllEntities() {
        try {
            List<YourEntity> entities = service.getAllEntities();
            return ResponseEntity.ok(entities);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<YourEntity> getEntityById(@PathVariable Long id) {
        try {
            YourEntity entity = service.getEntityById(id);
            return ResponseEntity.ok(entity);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<YourEntity> createEntity(@RequestBody YourEntity entity) {
        try {
            YourEntity created = service.createEntity(entity);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<YourEntity> updateEntity(
            @PathVariable Long id, 
            @RequestBody YourEntity entity) {
        try {
            YourEntity updated = service.updateEntity(id, entity);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntity(@PathVariable Long id) {
        try {
            service.deleteEntity(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
```

**Key Points:**
- Use `@RestController` annotation
- Use `@RequestMapping("/api/your-entities")` for the base path (use plural, kebab-case)
- Use appropriate HTTP method annotations:
  - `@GetMapping` - Retrieve resources
  - `@PostMapping` - Create resources
  - `@PutMapping` - Update resources (full update)
  - `@PatchMapping` - Partial updates (if needed)
  - `@DeleteMapping` - Delete resources
- Use `@PathVariable` for path parameters
- Use `@RequestBody` for request body (JSON)
- Return `ResponseEntity<T>` for proper HTTP status codes
- Handle exceptions appropriately:
  - `404 Not Found` for missing resources
  - `400 Bad Request` for validation errors
  - `201 Created` for successful creation
  - `204 No Content` for successful deletion
  - `500 Internal Server Error` for unexpected errors

### 5. Update Database Schema (if needed)

If you're creating a new table, add the schema definition to `src/main/resources/db/schema.sql`.

**Example:**

```sql
CREATE TABLE IF NOT EXISTS your_table_name (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    field_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    -- Add other columns as needed
    CONSTRAINT unique_field_name UNIQUE (field_name)
);
```

## Complete Example: Story Entity

Here's a complete example of adding a "Story" endpoint following all the steps:

### 1. Model (`Story.java`)

```java
package com.storykeeper.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "stories")
public class Story extends BaseEntity {

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    public Story() {
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
```

### 2. Repository (`StoryRepository.java`)

```java
package com.storykeeper.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.storykeeper.model.Story;

@Repository
public interface StoryRepository extends BaseRepository<Story, Long> {
    List<Story> findByUserId(Long userId);
}
```

### 3. Service (`StoryService.java`)

```java
package com.storykeeper.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.storykeeper.model.Story;
import com.storykeeper.repository.StoryRepository;

@Service
public class StoryService {
    
    @Autowired
    private StoryRepository repository;
    
    public List<Story> getAllStories() {
        return repository.findAll();
    }
    
    public List<Story> getStoriesByUserId(Long userId) {
        return repository.findByUserId(userId);
    }
    
    public Story getStoryById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Story not found with id: " + id));
    }
    
    public Story createStory(Story story) {
        return repository.save(story);
    }
    
    public Story updateStory(Long id, Story story) {
        Story existing = getStoryById(id);
        existing.setTitle(story.getTitle());
        existing.setContent(story.getContent());
        return repository.save(existing);
    }
    
    public void deleteStory(Long id) {
        repository.deleteById(id);
    }
}
```

### 4. Controller (`StoryController.java`)

```java
package com.storykeeper.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.storykeeper.model.Story;
import com.storykeeper.service.StoryService;

@RestController
@RequestMapping("/api/stories")
public class StoryController {

    @Autowired
    private StoryService service;

    @GetMapping
    public ResponseEntity<List<Story>> getAllStories(
            @RequestParam(required = false) Long userId) {
        try {
            List<Story> stories = userId != null 
                ? service.getStoriesByUserId(userId)
                : service.getAllStories();
            return ResponseEntity.ok(stories);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Story> getStoryById(@PathVariable Long id) {
        try {
            Story story = service.getStoryById(id);
            return ResponseEntity.ok(story);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<Story> createStory(@RequestBody Story story) {
        try {
            Story created = service.createStory(story);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Story> updateStory(
            @PathVariable Long id, 
            @RequestBody Story story) {
        try {
            Story updated = service.updateStory(id, story);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStory(@PathVariable Long id) {
        try {
            service.deleteStory(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
```

## Best Practices

1. **Naming Conventions:**
   - Controllers: `{Entity}Controller` (e.g., `StoryController`)
   - Services: `{Entity}Service` (e.g., `StoryService`)
   - Repositories: `{Entity}Repository` (e.g., `StoryRepository`)
   - Models: Singular noun (e.g., `Story`)
   - Endpoints: Plural, kebab-case (e.g., `/api/stories`)

2. **Error Handling:**
   - Always wrap operations in try-catch blocks
   - Return appropriate HTTP status codes
   - Consider creating custom exception classes for better error handling

3. **Validation:**
   - Add validation annotations to entity fields (`@NotNull`, `@Size`, etc.)
   - Use `@Valid` annotation in controller methods
   - Validate business rules in the service layer

4. **Security:**
   - Consider adding authentication/authorization as needed
   - Validate user permissions before operations
   - Sanitize user inputs

5. **Testing:**
   - Write unit tests for services
   - Write integration tests for controllers
   - Test error scenarios

## Common Patterns

### Query Parameters

Use `@RequestParam` for optional query parameters:

```java
@GetMapping
public ResponseEntity<List<Entity>> getAll(
        @RequestParam(required = false) String filter,
        @RequestParam(defaultValue = "10") int limit) {
    // Implementation
}
```

### Path Variables

Use `@PathVariable` for required path parameters:

```java
@GetMapping("/{id}")
public ResponseEntity<Entity> getById(@PathVariable Long id) {
    // Implementation
}
```

### Request Body

Use `@RequestBody` for JSON request bodies:

```java
@PostMapping
public ResponseEntity<Entity> create(@RequestBody Entity entity) {
    // Implementation
}
```

## Testing Your Endpoint

After creating your endpoint, test it using:

1. **cURL:**
   ```bash
   # GET request
   curl http://localhost:8080/api/your-entities
   
   # POST request
   curl -X POST http://localhost:8080/api/your-entities \
     -H "Content-Type: application/json" \
     -d '{"fieldName": "value"}'
   ```

2. **Postman or similar tools**

3. **Integration tests** (recommended)

## Summary Checklist

When adding a new endpoint, ensure you have:

- [ ] Created the Entity model extending `BaseEntity`
- [ ] Created the Repository interface extending `BaseRepository`
- [ ] Created the Service class (optional but recommended)
- [ ] Created the Controller with appropriate HTTP methods
- [ ] Updated database schema if needed
- [ ] Added proper error handling
- [ ] Tested the endpoint
- [ ] Followed naming conventions

## Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA Documentation](https://spring.io/projects/spring-data-jpa)
- [REST API Best Practices](https://restfulapi.net/)
