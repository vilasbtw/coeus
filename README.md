# Coeus
Coeus is a RESTful API developed in Java using Spring Boot, designed to simulate the backend of a fictional academic library system. The main goal of this project is to explore best practices with Spring Boot for building well-structured RESTful APIs.

## Technologies Used:

- Java
- Spring Boot
- Maven
- MySQL
- Lombok
- MapStruct
- Postman
- JUnit
- Mockito

## Install:

### Prerequisites:
In order to use the API, make sure you have the following programs installed:
- Java 17+
- MySQL
- Maven
- Postman (recommended)

### 1. Clone this repo

```bash
git clone https://github.com/vilasbtw/coeus.git
```

### 2. Set up the `application.properties`
By default, the database name is `coeus`, the user is `root`, and there is no password.
You can change these settings according to your local MySQL setup.

Update the following properties in `src/main/resources/application.properties`:

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

### 5. Test the requests
To verify that the application is correctly managing data, you can use tools like Postman to send HTTP requests. Below are examples of common operations:

- **Find a book by id:**
**GET**
```bash
localhost:8080/books/1
```

- **Insert a new book:**
**POST**
```bash
localhost:8080/books
```
```json
{
   "bookName": "The Stranger",
   "authorName": "Albert Camus",
   "publisherName": "Record Books",
   "numberOfPages": 150,
   "genre": "Philosophy",
   "price": 15.0
}
```

**Update a book info:**
**PUT**
```bash
localhost:8080/books
```
```json
{
   "id": 2,		
   "bookName": "The Stranger",
   "authorName": "Albert Camus",
   "publisherName": "Vintage",
   "numberOfPages": 150,
   "genre": "Philosophy",
   "price": 10.0
}
```

**Delete a book:**
**DELETE**
```bash
localhost:8080/books/1
```

**Retrieve all the books:**
**GET**
```bash
localhost:8080/books/
```

## To-do:

### Completed:
- [x] Created `Book` entity
- [x] Implemented MVC pattern (`BookController`, `BookService`, `BookRepository`)
- [x] Connected the API to a MySQL database with Spring Data JPA 
- [x] Implemented CRUD operations (`create(bookDTO)`, `findById(id)`, `findAll()`, `update(bookDTO)` and `delete(id)`)
- [x] Added exception handling with custom error responses
- [x] Applied the DTO pattern to manage Data Transfer Objects
- [x] Add support for database migrations with Flyway
- [x] Enable content negotiation (support for XML and YAML)

### Next Features:
- [ ] Implement HATEOAS
- [ ] Document API with Swagger
- [ ] Write unit tests using JUnit and Mockito for the BookService class
- [ ] Implement the Person endpoint
- [ ] Write integration tests with Testcontainers and REST Assured
- [ ] Configure and enable CORS
