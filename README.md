# Book API - Spring Boot Application
## Overview
This Spring Boot application provides a RESTful API for managing book objects. It supports basic CRUD operations to create, read, update, and delete books.

## Features
- Create a new book
- Retrieve a list of all books
- Retrieve details of a specific book by ID
- Update a book's information
- Delete a book

## Technologies Used
- Java 8 or higher
- Spring Boot
- Spring Data JPA
- Hibernate
- H2 Database (for development)
- Maven

## Getting Started
### Prerequisites
- Java 8 or higher
- Maven

## Installation
Clone the repository:

```
bash
Copy code
git clone https://github.com/yourusername/book-api.git
cd book-api
```

## Build the project using Maven:

```
bash
Copy code
mvn clean install
```

## Run the application:

```
bash
Copy code
mvn spring-boot:run
```

## Accessing the API
Once the application is running, you can access the API at http://localhost:8080/api/books.

## API Endpoints
Create a new book

- URL: /api/books
- Method: POST
- Request Body:

```
json
Copy code
{
  "isbn": "ISBN Number",
  "author": "Author Name",
  "title": "Book Title"
}
```
Response:
```
json
Copy code
{
  "isbn": "ISBN Number",
  "author": "Author Name",
  "title": "Book Title"
}
```
Retrieve a list of all books
- URL: /api/books
- Method: GET
- Response:
```
json
Copy code
[
  {
    "isbn": "ISBN Number",
    "author": "Author Name",
    "title": "Book Title"
  },
  ...
]
```
Retrieve a specific book by ID
- URL: /api/books/{id}
- Method: GET
- Response:
```
json
Copy code
{
  "isbn": "ISBN Number",
  "author": "Author Name",
  "title": "Book Title"
}
```
Update a book's information
- URL: /api/books/{id}
- Method: PUT
- Request Body:
```
json
Copy code
{
  "isbn": "Updated ISBN",
  "title": "Updated Title",
  "author": "Updated Author"
}
```
- Response:
```
json
Copy code
{
  "isbn": "Updated ISBN",
  "title": "Updated Title",
  "author": "Updated Author"
}
```
Delete a book
- URL: /api/books/{id}
- Method: DELETE
- Response: 204 No Content

## Database Configuration
The application uses an in-memory H2 database by default. You can access the H2 console at http://localhost:8080/h2-console with the following credentials:

- JDBC URL: jdbc:h2:mem:testdb
- Username: sa
- Password: password

## Running Tests
To run the tests, use the following command:
```
bash
Copy code
mvn test
```
## Contributing
1. Fork the repository.
2. Create a new branch (git checkout -b feature-branch).
3. Commit your changes (git commit -am 'Add new feature').
4. Push to the branch (git push origin feature-branch).
5. Create a new Pull Request.

### License
This project is licensed under the MIT License. See the LICENSE file for details.

### Contact
For any questions or suggestions, check out my LinkedIn: https://www.linkedin.com/in/chris-howell-b0ba73150/.

