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
In order to use Coeus API, make sure you have the following installed:
- Java 17+
- MySQL
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
> _This step won’t be necessary in the future, since we plan to support migrations with Flyway. Once implemented, the database will be automatically created when the application runs._

For now, please run the following SQL code to set up the database:

```sql
CREATE DATABASE coeus;

USE coeus;

CREATE TABLE book (
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	book_name VARCHAR(255) NOT NULL,
	author_name VARCHAR(255) NOT NULL,
	publisher_name VARCHAR(255) NOT NULL,
	number_of_pages INT NOT NULL,
	genre VARCHAR(255) NOT NULL,
	price DOUBLE NOT NULL
);

INSERT INTO book
(book_name, author_name, publisher_name, number_of_pages, genre, price)
VALUES
('Crime and Punishment', 'Fyodor Dostoevsky', 'Penguin Books', 600, 'Drama', 20);
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

### Next Features:
- [ ] Write unit tests using JUnit and Mockito
- [ ] Add support for database migrations with Flyway
- [ ] Enable content negotiation (support for XML and YAML)
- [ ] Implement HATEOAS
- [ ] Document API with Swagger
- [ ] Write integration tests with Testcontainers and REST Assured
- [ ] Configure and enable CORS