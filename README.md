# Articles API 🚀

A complete implementation of a secure REST API for creating and managing articles, featuring JWT authentication and a role-based access system.

## About The Project 📖

This project demonstrates:

- **🔐 JWT Authentication** - Secure API access with tokens
- **👥 Role-Based System** - Separation of permissions between users and administrators
- **📝 CRUD Operations** - Creating and retrieving articles with pagination
- **📊 Statistics** - An admin panel with author analytics
- **🗄️ Relational DB** - PostgreSQL with Liquibase for schema versioning
- **🧪 Testing** - A full suite of unit and integration tests

## Key Features ✨

### 🔐 Security & Authentication
- **JWT tokens** for secure authentication
- **BCrypt** for password hashing
- **Role-based system** (USER, ADMIN) with `@PreAuthorize`
- **Stateless sessions** for scalability

### 📝 Article Management
- **Article creation** with validation for all fields
- **Pagination** for efficient loading
- **Uniqueness** based on the title + author combination
- **ISO 8601** format for publication dates

### 📊 Admin Panel
- **Top-3 authors** by article count in the last 50 days
- **Statistics** accessible only to administrators
- **Optimized queries** using JPQL

### 🗄️ Database
- **PostgreSQL 17** with timezone support
- **Liquibase** for schema versioning
- **Soft delete** for safe record removal
- **Indexes** for fast searching

## Project Structure 📁

```
src/main/java/org/cyberrealm/tech/
├── config/                              # Spring Configuration
│   ├── SecurityConfig.java              # Security settings
│   └── OpenApiConfig.java               # Swagger documentation
├── controller/                          # REST Controllers
│   ├── ArticleController.java           # Article management
│   ├── AuthenticationController.java    # Authentication
│   └── StatsController.java             # Statistics
├── model/                               # JPA Entities
│   ├── Article.java                     # Article entity
│   ├── User.java                        # User entity
│   └── Role.java                        # User roles
├── service/                             # Business Logic
│   ├── ArticleService.java              # Article service
│   └── UserService.java                 # User service
├── repository/                          # JPA Repositories
│   ├── ArticleRepository.java           # Article repository
│   └── UserRepository.java              # User repository
├── security/                            # Security
│   ├── JwtUtil.java                     # JWT utilities
│   └── JwtAuthenticationFilter.java     # JWT filter
└── dto/                                 # Data Transfer Objects
    ├── article/                         # DTOs for articles
    └── user/                            # DTOs for users
```

## API Endpoints 🌐

### 🔐 Authentication
```
POST /api/auth/sign-up          # Register a new user
POST /api/auth/sign-in          # Log in
```

### 📝 Articles
```
POST   /api/articles            # Create an article (USER/ADMIN)
GET    /api/articles            # Get articles with pagination (USER/ADMIN)
```

### 📊 Statistics
```
GET    /api/stats/top-authors   # Top-3 authors (ADMIN only)
```

## Getting Started 🚀

### Prerequisites
- **Java 21+**
- **Maven 3.6+**
- **Docker & Docker Compose**
- **PostgreSQL 17**

### Instructions

1. **Clone and build**:
```bash
    git clone https://github.com/4Vitalii5/articles-api.git
    cd articles-api
    mvn clean install
```

2. **Start the database**:
```bash
    docker-compose up -d
```

3. **Run the server**:
```bash
    mvn spring-boot:run
```

4. **Available endpoints**:
- API: `http://localhost:8080/api`
- Swagger UI: `http://localhost:8080/api/swagger-ui`

4. **Created bruno collection in project root folder for test purpose**:
- Created all possible test endpoints with test data and scripts

## API Usage 💻

### 🔐 User Registration
```bash
    curl -X POST http://localhost:8080/api/auth/sign-up \
      -H "Content-Type: application/json" \
      -d '{
        "email": "user@example.com",
        "password": "password123",
        "repeatPassword": "password123",
        "firstName": "John",
        "lastName": "Doe"
      }'
```

### 🔐 Login
```bash
    curl -X POST http://localhost:8080/api/auth/sign-in \
      -H "Content-Type: application/json" \
      -d '{
        "email": "user@example.com",
        "password": "password123"
      }'
```

### 📝 Create an Article
```bash
    curl -X POST http://localhost:8080/api/articles \
      -H "Authorization: Bearer YOUR_JWT_TOKEN" \
      -H "Content-Type: application/json" \
      -d '{
        "title": "My First Article",
        "author": "John Doe",
        "content": "This is the content of my article...",
        "publishDate": "2024-01-15T10:00:00Z"
      }'
```

### 📊 Get Statistics (ADMIN)
- **Admin login**: admin@mail.com
- **Admin password**: adminPassword
```bash
    curl -X GET http://localhost:8080/api/stats/top-authors \
      -H "Authorization: Bearer ADMIN_JWT_TOKEN"
```

## Project Expansion 🚀

### Possible Improvements
1. **Caching** - Redis for caching articles
2. **OpenAPI** - Add openAPI generation and refactor logic
3. **Google Authorization** - Add authorization by third API
4. **Search** - Elasticsearch for full-text search
5. **Files** - Uploading images for articles
6. **Comments** - A commenting system for articles
7. **Ratings** - User ratings for articles
8. **Categories** - Classifying articles by topics

---

**Author**: **Vitalii Pavlyk** - Java Developer ([LinkedIn](https://www.linkedin.com/in/vitalii-pavlyk-82b5aa1a1/), [GitHub](https://github.com/4Vitalii5))  
**Technologies**: Java 21, Spring Boot, PostgreSQL, JWT, JUnit  
**License**: [LICENSE](LICENSE) 