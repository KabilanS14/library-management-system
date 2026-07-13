# Library Management System Backend

![Java](https://img.shields.io/badge/Java-17-blue) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen) ![MySQL](https://img.shields.io/badge/MySQL-8.x-orange) ![Maven](https://img.shields.io/badge/Maven-3.x-red)

## Project Overview
A production-ready backend for a library management system built with Java 17, Spring Boot 3, Spring Security, JWT, Spring Data JPA, MySQL, and Swagger/OpenAPI.

## Features
- JWT authentication and authorization
- Role-based access for ADMIN and MEMBER
- Book CRUD, search, and pagination
- Borrow and return workflows with fine calculation
- Swagger UI documentation
- Sample data initialization
- Unit and integration tests

## Tech Stack
- Java 17
- Spring Boot 3.3
- Spring Security
- Spring Data JPA
- Hibernate
- MySQL
- JWT
- Maven
- Lombok
- SpringDoc OpenAPI

## Architecture
- Controller
- Service
- Repository
- Entity
- DTO
- Exception
- Security
- Config
- Util

## Folder Structure
```text
src/
  main/
    java/com/librarymanagement/backend/
      config/
      controller/
      dto/
      entity/
      enums/
      exception/
      repository/
      security/
      service/
      util/
    resources/
      application.properties
      application-dev.properties
      application-prod.properties
      data.sql
  test/
    java/com/librarymanagement/backend/
      controller/
      service/
postman/
  Library-Management-System.postman_collection.json
```

## Database Schema
- User
- Book
- BorrowRecord

## Entity Relationship Diagram
```mermaid
ergraph TD
A[User] -->|1..*| B[BorrowRecord]
C[Book] -->|1..*| B[BorrowRecord]
```

## API Endpoints
| Method | Endpoint | Access |
|---|---|---|
| POST | /auth/register | PUBLIC |
| POST | /auth/login | PUBLIC |
| POST | /books | ADMIN |
| GET | /books | ADMIN/MEMBER |
| GET | /books/{id} | ADMIN/MEMBER |
| PUT | /books/{id} | ADMIN |
| DELETE | /books/{id} | ADMIN |
| GET | /books/search | ADMIN/MEMBER |
| POST | /borrow/{bookId} | MEMBER |
| POST | /return/{borrowId} | MEMBER |
| GET | /borrow/history | MEMBER |
| GET | /borrow/current | MEMBER |
| GET | /admin/borrow-records | ADMIN |
| GET | /admin/borrow-records/overdue | ADMIN |
| GET | /admin/borrow-records/fines | ADMIN |

## Authentication Flow
1. User registers or logs in.
2. The backend returns a JWT.
3. The JWT is sent in the Authorization header.
4. Spring Security validates the token for secured routes.

## Screenshots
- Placeholder: screenshots/architecture.png
- Placeholder: screenshots/swagger.png

## Installation Guide
1. Install Java 17 and Maven.
2. Create a MySQL database named library_management_db.
3. Update credentials in application-dev.properties.
4. Run `mvn clean install`.
5. Start the app with `mvn spring-boot:run`.

## Configuration
- Default profile: application.properties
- Development profile: application-dev.properties
- Production profile: application-prod.properties

## Running the Application
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## Testing with Postman
- Import the collection from postman/Library-Management-System.postman_collection.json.
- Set the baseUrl variable to http://localhost:8080.
- Login to receive a JWT and store it in jwtToken.

## Swagger Documentation
Open: http://localhost:8080/swagger-ui/index.html

## Future Enhancements
- Email notifications
- Fine payment integration
- Admin dashboard
- Audit logs

## Author
Kabilan

## License
MIT
