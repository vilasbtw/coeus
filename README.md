# Coeus
Coeus is a web application designed to manage books within a library system, enabling efficient control of book loans. This RESTful API is being developed in Java using Spring Boot.

## Technologies Used:

- Java
- Spring Boot
- Maven
- MySQL
- MapStruct
- Postman
- JUnit
- Mockito
- Swagger
- Testcontainers

## Install:

### 0. Prerequisites:
In order to use the API, make sure you have the following programs installed:
- Java 17+
- MySQL
- Maven
- Postman (recommended for managing HTTP requests)

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
   "id": 1,
   "bookName": "The Brothers Karamazov",
   "authorName": "Fyodor Dostoevsky",
   "publisherName": "Penguin Classics",
   "numberOfPages": 796,
   "genre": "Detective novel",
   "price": 30.0
}
```

**Delete a book:**  
**DELETE**
```bash
localhost:8080/books/2
```

**Retrieve all the books:**  
**GET**
```bash
localhost:8080/books/
```

## To-do:

### Next Features:
- [ ] Configure and enable CORS
- [ ] Update the tables so they match the modeling
- [ ] Add pagination support to student listing 
- [ ] Add JWT and Spring Security
- [ ] Write integration tests with Testcontainers and REST Assured
- [ ] Implement the logic of renting books
- [ ] Implement warning and penalty system
- [ ] Add front-end part
- [ ] Deploy API to cloud
- [ ] Add MIT license

### Completed:
- [x] Created `Book` entity
- [x] Implemented MVC pattern (`BookController`, `BookService`, `BookRepository`)
- [x] Connected the API to a MySQL database with Spring Data JPA 
- [x] Implemented CRUD operations (`create(bookDTO)`, `findById(id)`, `findAll()`, `update(bookDTO)` and `delete(id)`)
- [x] Added exception handling with custom error responses
- [x] Applied the DTO pattern to manage Data Transfer Objects
- [x] Added support for database migrations with Flyway
- [x] Enabled content negotiation (support for XML and YAML)
- [x] Implemented HATEOAS
- [x] Written unit tests using JUnit and Mockito for the BookService class
- [x] Documented the API with Swagger
- [x] Remodeled the database
- [x] Implemented the `Student` endpoints
- [x] Implemented the `Employee` endpoints
