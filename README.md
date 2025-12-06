SpringBootManyToOne
===================

A simple CRUD web application built with Spring Boot, Spring MVC, Thymeleaf, and Spring Data JPA that demonstrates a Many-to-One relationship between `Employee` (many) and `Department` (one). The UI uses Bootstrap and server-side rendering via Thymeleaf templates.

Features
--------
- Manage Departments: create, list, edit, delete
- Manage Employees: create, list, edit, delete
- Validation on forms (JSR-380)
- Search Employees by:
  - Employee name
  - Department name
- Many-to-One mapping: each Employee belongs to a Department

Tech Stack
---------
- Java (configured for JDK 25 in `pom.xml`)
- Spring Boot 4.x (MVC, Thymeleaf, Data JPA, Validation)
- PostgreSQL (JDBC driver)
- Maven Wrapper (`mvnw`)
- Bootstrap 5 (static assets included)

Prerequisites
-------------
- JDK 25 (or adjust `<java.version>` in `pom.xml` to match your installed JDK, e.g., 21 or 17)
- Maven (optional - repo includes Maven Wrapper)
- PostgreSQL running locally

Getting Started
---------------

1) Clone the repository
```
git clone <your-repo-url>
cd SpringBootManyToOne
```

2) Configure the database connection in `src/main/resources/application.properties` (defaults shown):
```
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=chintanpatel
spring.datasource.password=postgres

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgresPlusDialect
spring.jpa.properties.hibernate.hbm2ddl.auto=update
spring.jpa.properties.hibernate.show_sql=true
spring.jpa.properties.hibernate.format_sql=true

spring.thymeleaf.cache=false
server.port=8080
```
Notes:
- `spring.jpa.properties.hibernate.hbm2ddl.auto=update` will auto-create/update tables on startup. For production, consider using Flyway/Liquibase instead.
- You can override properties via environment variables, for example:
  - `SPRING_DATASOURCE_URL`
  - `SPRING_DATASOURCE_USERNAME`
  - `SPRING_DATASOURCE_PASSWORD`
  - `SERVER_PORT`

3) Start the application
```
./mvnw spring-boot:run
```
Then open: http://localhost:8080

Running Tests
-------------
```
./mvnw test
```

Application Endpoints
---------------------

Department
- GET `/departments` - list departments
- GET `/departments/create` - show create form
- POST `/departments/insertOrUpdateDepartment` - create or update
- GET `/departments/manageDepartment/{id}` - edit existing
- GET `/departments/deleteDepartment/{id}` - delete

Employee
- GET `/employees` - list employees
- GET `/employees/create` - show create form
- POST `/employees/insertOrUpdateEmployee` - create or update
- GET `/employees/manageEmployee/{id}` - edit existing
- GET `/employees/deleteEmployee/{id}` - delete
- GET `/employees/search/employeeName?employeeName=...` - search by employee name
- GET `/employees/search/departmentName?departmentName=...` - search by department name

UI Templates
------------
- `src/main/resources/templates/department/*.html`
- `src/main/resources/templates/employee/*.html`
- `src/main/resources/templates/index.html`

Static Bootstrap assets are under `src/main/resources/static`.

Data Model Overview
-------------------
- `Department` - parent entity
- `Employee` - child entity with a `ManyToOne` association to `Department`

Common Issues and Tips
----------------------
- If you do not have JDK 25 installed, edit `<java.version>` in `pom.xml` to match your JDK (e.g., 21 or 17), then rebuild.
- Ensure PostgreSQL is running and credentials in `application.properties` are correct.
- If port 8080 is in use, set `SERVER_PORT=8081` (or change `server.port` in properties).

License
-------
This project is provided as-is for learning and demonstration purposes.