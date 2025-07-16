# Coeus
Coeus is a web application designed to manage books in a library system, allowing efficient control over book loans.

## Technologies Used

**Backend:**
- Java
- Spring Boot
- Maven
- MySQL
- MapStruct

**Testing:**
- JUnit
- Mockito
- Testcontainers
- Postman

**Documentation & Tools:**
- Swagger
- Docker
- AWS
- GCP

**Frontend:**
- React

## Install

### 0. Prerequisites:
In order to use the API, make sure you have the following programs installed:
- Java 21
- MySQL
- Maven
- Postman or Insomnia (recommended for managing HTTP requests)
- Docker (required for running integration tests)

### 1. Clone this repo

```bash
git clone https://github.com/vilasbtw/coeus.git
```

### 2. Set up the `application.properties`
By default, the database name is `coeus`, the user is `root`, and there is no password.
You can change these settings according to your local MySQL setup.

Update the following properties in `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/coeus
spring.datasource.username=root
spring.datasource.password=
```

### 3. Set up the database
Please run the following SQL code to create the database:

```sql
CREATE DATABASE coeus;
```

### 4. Run the application
Run `CoeusApplication.java`

### 5. Send the requests
To verify that the application is correctly managing data, you can use tools like **Postman** or **Insomnia** to send HTTP requests.

You might find it useful to import the Postman collection located at:
`coeus/docs/postman`

Below are examples of common operations:

- **Register**   
**[POST]**
```bash
http://localhost:8080/auth/register

{
   "username": "kvilas",
   "password": "123",
   "role": "COORDINATOR",
   "employee_id": 1
}
```
- **Login**   
**[POST]**
```bash
http://localhost:8080/auth/login

{
   "username": "kvilas",
   "password": "123"
}
```

- **Refresh Token**   
**[POST]**
```bash
http://localhost:8080/auth/refresh-token

{
    "refreshToken": "..."
}
```

- **Find a book by id:**  
**[GET]**
```bash
http://localhost:8080/books/1
```

- **Insert a new book:**   
**[POST]**
```bash
http://localhost:8080/books

{
   "bookName": "The Stranger",
   "authorName": "Albert Camus",
   "publisherName": "Record Books",
   "numberOfPages": 150,
   "genre": "Philosophy",
   "price": 15.0
}
```

- **Update a book info:**  
**[PUT]**
```bash
http://localhost:8080/books

{
   "id": 1,
   "bookName": "The Brothers Karamazov",
   "authorName": "Fyodor Dostoevsky",
   "publisherName": "Penguin Classics",
   "numberOfPages": 796,
   "genre": "Detective novel",
   "price": 30.0
}
```

- **Delete a book:**  
**[DELETE]**
```bash
http://localhost:8080/books/2
```

- **Retrieve all the books:**  
**[GET]**
```bash
http://localhost:8080/books?page=1&size=12&direction=asc
```

- **Disable a student:**  
**[PATCH]**
```bash
http://localhost:8080/students/4
```

- **Find students by name:**  
**[GET]**
```bash
http://localhost:8080/students/findByName/maria
```

## Running Tests

This project includes both **unit tests** and **integration tests** to ensure code quality and correct behavior of the application.

- **Unit tests** focus on individual components (services, mappers, etc.), using tools like **JUnit** and **Mockito**.
- **Integration tests** validate the interaction between components and external resources (e.g., database, REST API), using **Testcontainers** to create isolated environments.

> To run the integration tests successfully, make sure **Docker** is installed and running on your machine!

## To-do

### Next Features:

- [ ] Implement the logic of renting books;
- [ ] Implement warning and penalty system;
- [ ] Add React-based front-end;
- [ ] Dockerize the application;
- [ ] Deploy the API on AWS;
- [ ] Continous Deployment AWS with GitHub Actions;
- [ ] Deploy the API on GCP;
- [ ] Continous Deployment GCP with GitHub Actions;
- [ ] Add MIT license;

### Completed:
- [x] Created `Book` entity;
- [x] Implemented MVC pattern (`BookController`, `BookService`, `BookRepository`);
- [x] Connected the API to a MySQL database with Spring Data JPA;
- [x] Implemented CRUD operations (`create(bookDTO)`, `findById(id)`, `findAll()`, `update(bookDTO)` and `delete(id)`);
- [x] Added exception handling with custom error responses;
- [x] Applied the DTO pattern to manage Data Transfer Objects;
- [x] Added support for database migrations with Flyway;
- [x] Enabled content negotiation (support for XML and YAML);
- [x] Implemented HATEOAS;
- [x] Written unit tests using JUnit and Mockito for the BookService class;
- [x] Documented the API with Swagger;
- [x] Remodeled the database;
- [x] Implemented the `Student` endpoints;
- [x] Implemented the `Employee` endpoints;
- [x] Written integration tests with Testcontainers and REST Assured;
- [x] Configured and enable CORS;
- [x] Added pagination support to student listing;
- [x] Add Spring Security + JWT;
- [x] Implement Integration Tests for Spring Security;